package com.gcloud.controller.compute.service.vm.create.impl;

import com.gcloud.common.util.NetworkUtil;
import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.ComputeNodeDao;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dao.InstanceTypeDao;
import com.gcloud.controller.compute.dao.ZoneInstanceTypeDao;
import com.gcloud.controller.compute.dispatcher.Dispatcher;
import com.gcloud.controller.compute.entity.ComputeNode;
import com.gcloud.controller.compute.entity.InstanceType;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.entity.ZoneInstanceTypeEntity;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.model.vm.CreateInstanceByImageInitParams;
import com.gcloud.controller.compute.model.vm.CreateInstanceByImageInitResponse;
import com.gcloud.controller.compute.model.vm.VmImageInfo;
import com.gcloud.controller.compute.service.vm.create.IVmCreateService;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.compute.utils.VmControllerUtil;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.storage.dao.DiskCategoryDao;
import com.gcloud.controller.storage.dao.StoragePoolDao;
import com.gcloud.controller.storage.entity.DiskCategory;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.BooleanType;
import com.gcloud.header.compute.enums.CreateType;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.compute.enums.UseDepartmentType;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.enums.VmTaskState;
import com.gcloud.header.storage.enums.StoragePoolDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
@Slf4j
public class VmCreateServiceImpl implements IVmCreateService{
	@Autowired
	private InstanceDao vmInstanceDao;

	@Autowired
    private StoragePoolDao storagePoolDao;

	@Autowired
    private ImageDao imageDao;

	@Autowired
    private SubnetDao subnetDao;

	@Autowired
    private InstanceTypeDao instanceTypeDao;

	@Autowired
    private ComputeNodeDao computeNodeDao;

	@Autowired
    private DiskCategoryDao diskCategoryDao;

	@Autowired
    private IVolumeService volumeService;

	@Autowired
    private ZoneInstanceTypeDao zoneInstanceTypeDao;

    @Override
	public CreateInstanceByImageInitResponse createInstanceByImageInit(CreateInstanceByImageInitParams params, CurrentUser currentUser){

	    Image image = imageDao.getById(params.getImageId());
        if (image == null) {
			throw new GCloudException("compute_controller_vm_011005::can not find the image");
		}

        StoragePool pool = this.storagePoolDao.checkAndGet(params.getZoneId(), params.getSystemDiskCategory());
        DiskCategory sysCategory = diskCategoryDao.getById(params.getSystemDiskCategory());

        StorageType storageType = StorageType.value(pool.getStorageType());
        StoragePoolDriver storageDriver = StoragePoolDriver.get(pool.getDriver());
        Map<String, DiskCategory> categoryMap = new HashMap<>();
        categoryMap.put(sysCategory.getId(), sysCategory);


        int imageSize = 0;
        if (image.getMinDisk() > 0) {
            imageSize = image.getMinDisk().intValue();
        }
        else {
            imageSize = (int)Math.ceil(image.getSize() / 1024.0 / 1024.0 / 1024.0);
        }

        if (imageSize == 0) {
            throw new GCloudException("0010111::无法获取到镜像大小");
        }

        Integer sysSize = null;

        if (params.getSystemDiskSize() == null || params.getSystemDiskSize() == 0) {
            sysSize = imageSize;
        } else {
            sysSize = params.getSystemDiskSize();
        }

        if(sysSize < imageSize){
            throw new GCloudException("0010109::系统盘大小不能小于镜像大小");
        }

        if((sysCategory.getMaxSize() != null && sysSize > sysCategory.getMaxSize()) || (sysCategory.getMinSize() != null && sysSize < sysCategory.getMinSize())){
            throw new GCloudException("0010110::系统盘大小不在磁盘类型允许范围内");
        }


        volumeService.checkCategory(params.getDataDisk());

        if(StringUtils.isNotBlank(params.getSubnetId())){
            Subnet subnet = subnetDao.getById(params.getSubnetId());
            if(subnet == null){
                throw new GCloudException("0010112::子网不存在");
            }

            if(StringUtils.isNotBlank(params.getIpAddress())){
                if(!NetworkUtil.checkCidrIp(subnet.getCidr(), params.getIpAddress())){
                    throw new GCloudException("0010113::ip无效");
                }
            }
        }

        InstanceType instanceType = instanceTypeDao.getById(params.getInstanceType());
        if(instanceType == null){
            throw new GCloudException("0010114::找不到实例类型");
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put(ZoneInstanceTypeEntity.INSTANCE_TYPE_ID, instanceType.getId());
        filters.put(ZoneInstanceTypeEntity.ZONE_ID, params.getZoneId());

        ZoneInstanceTypeEntity typeZone = zoneInstanceTypeDao.findUniqueByProperties(filters);

        if(typeZone == null){
            throw new GCloudException("0010115::找不到实例类型");
        }

        //暂时 volume和image的driver需要一样
        VmImageInfo imageCreateInfo = VmControllerUtil.getVmImageInfo(storageDriver,  storageDriver,null, params.getImageId(), null, CreateType.IMAGE);


        Integer cpu = instanceType.getVcpus();
        Integer memory = instanceType.getMemoryMb();

        VmInstance vmIns = new VmInstance();

        vmIns.setId(UUID.randomUUID().toString());
        vmIns.setImageId(params.getImageId());
        vmIns.setUserId(currentUser.getId());
        vmIns.setCreator(currentUser.getId());
        vmIns.setCore(cpu);
        vmIns.setMemory(memory);
        vmIns.setInstanceType(instanceType.getId());

        vmIns.setDisk(sysSize * 1024);

        vmIns.setImagePoolId(imageCreateInfo.getImagePoolId());
        vmIns.setImageStorageType(imageCreateInfo.getImageStorageType());
        vmIns.setState(VmState.PENDING.value());
        vmIns.setLastState(VmState.PENDING.value());
        vmIns.setTaskState(VmTaskState.PENDING.value());
        vmIns.setCreateSourceId(params.getImageId());
        vmIns.setCreateType(CreateType.IMAGE.getValue());
        vmIns.setStorageType(storageType.getValue());
        vmIns.setIsFt(BooleanType.FALSE.getValue());
        vmIns.setIsHa(BooleanType.FALSE.getValue());
        vmIns.setLaunchTime(new Date());
        vmIns.setAutostart(BooleanType.FALSE.getValue());
        vmIns.setUsbRedir(BooleanType.FALSE.getValue());
        vmIns.setAlias(params.getInstanceName());
        vmIns.setUseDepartment(UseDepartmentType.GCLOUD.getValue());
        vmIns.setUsbType(3);
        vmIns.setZoneId(params.getZoneId());
        vmIns.setTenantId(currentUser.getDefaultTenant());

        String createHost = params.getCreateHost();

        if(StorageType.LOCAL.getValue().equals(pool.getStorageType())){

            if(StringUtils.isBlank(pool.getHostname())){
                throw new GCloudException("::存储池信息有误");
            }

            if(StringUtils.isBlank(createHost)){
                createHost = pool.getHostname();
            }else if(!pool.getHostname().equals(createHost)){
                throw new GCloudException("::不支持此节点创建");
            }
        }

        if(StringUtils.isNotBlank(createHost)){
            Node node = RedisNodesUtil.getComputeNodeByHostName(createHost);
            if(node == null){
                throw new GCloudException("0010116::节点不存在");
            }
            ComputeNode computeNode = computeNodeDao.findUniqueByProperty(ComputeNode.HOSTNAME, node.getHostName());
            if (!StringUtils.equals(vmIns.getZoneId(), computeNode.getZoneId())) {
                throw new GCloudException("0010117::该可用区没有所选的节点");
            }

            if(params.getHandleResource() != null && params.getHandleResource()){
                //占用资源
                Dispatcher.dispatcher().assignNode(createHost, cpu, memory);
            }

        }else{
            //占用资源
            Node node = Dispatcher.dispatcher().assignNodeInZone(vmIns.getZoneId(), cpu, memory);
            if(node == null){
                throw new GCloudException("0010118::节点资源不足");
            }

            createHost = node.getHostName();
        }

        vmIns.setHostname(createHost);

        try{
            vmInstanceDao.save(vmIns);
        }catch (Exception ex){
            log.error("保存数据库失败", ex);
            Dispatcher.dispatcher().release(createHost, cpu, memory);
            throw new GCloudException("0010119::创建云服务器系统异常");
        }


        CreateInstanceByImageInitResponse response = new CreateInstanceByImageInitResponse();
        response.setId(vmIns.getId());
        response.setCreateHost(createHost);
        response.setStorageType(storageType.getValue());
        response.setImageInfo(imageCreateInfo);
        response.setCpu(cpu);
        response.setMemory(memory);
        response.setSystemDiskSize(sysSize);
        response.setCreateUser(currentUser);

        return response;
    }



}
