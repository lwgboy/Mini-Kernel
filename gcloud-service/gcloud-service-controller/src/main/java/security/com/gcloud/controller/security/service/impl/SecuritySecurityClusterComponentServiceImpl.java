package com.gcloud.controller.security.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dao.InstanceTypeDao;
import com.gcloud.controller.compute.entity.InstanceType;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.security.dao.SecurityClusterComponentDao;
import com.gcloud.controller.security.entity.SecurityClusterComponent;
import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.enums.SecurityClusterComponentState;
import com.gcloud.controller.security.enums.SecurityComponent;
import com.gcloud.controller.security.model.ClusterCreateVmInfo;
import com.gcloud.controller.security.model.ComputeClusterCreateObjectInfo;
import com.gcloud.controller.security.model.VmCreateInfo;
import com.gcloud.controller.security.service.ISecurityClusterComponentService;
import com.gcloud.controller.security.util.SecurityUtil;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.model.VolumeAttachInfo;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.Device;
import com.gcloud.header.compute.msg.api.model.DiskInfo;
import com.gcloud.header.security.msg.model.CreateClusterDcParams;
import com.gcloud.header.security.msg.model.CreateClusterInfoParams;
import com.gcloud.header.security.msg.model.CreateClusterObjectParams;
import com.gcloud.header.security.msg.model.CreateClusterVmParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SecuritySecurityClusterComponentServiceImpl implements ISecurityClusterComponentService {


    @Autowired
    private SecurityClusterComponentDao securityClusterComponentDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private InstanceTypeDao instanceTypeDao;

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private VolumeDao volumeDao;

    public ComputeClusterCreateObjectInfo createClusterComponentVm(String clusterId, String clusterName, CurrentUser loginUser, CreateClusterInfoParams clusterInfoParams, CreateClusterVmParams vmParams, SecurityComponent type, Boolean isHa){


        VmCreateInfo createInfo = checkVmCreateInfo(vmParams, type);

        SecurityClusterComponent clusterComponent = new SecurityClusterComponent();
        clusterComponent.setId(UUID.randomUUID().toString());
        clusterComponent.setClusterId(clusterId);
        clusterComponent.setState(SecurityClusterComponentState.CREATING.value());
        clusterComponent.setHa(isHa);
        clusterComponent.setType(type.value());
        clusterComponent.setCreateUser(loginUser.getId());
        clusterComponent.setCreateTime(new Date());
        clusterComponent.setUpdateTime(new Date());
        clusterComponent.setObjectType(SecurityClusterComponentObjectType.VM.value());


        //工作流暂时没有重试，保存之前参数交给工作流
//        ComponentVmCreateConfig createConfig = new ComponentVmCreateConfig();
//        createConfig.setClusterCreateInfo(clusterInfoParams);
//        createConfig.setVmConfig(vmParams);
//        clusterComponent.setCreateConfig(JSON.toJSONString(createConfig));

        securityClusterComponentDao.save(clusterComponent);

//        String name = StringUtils.isBlank(clusterName) ? clusterId : clusterName;

        String instanceName = SecurityUtil.getAlias(clusterId, clusterName, type, isHa);

        ClusterCreateVmInfo createVmInfo = new ClusterCreateVmInfo();
        createVmInfo.setInstanceName(instanceName);
        createVmInfo.setSystemDiskCategory(vmParams.getSystemDiskCategory());
        createVmInfo.setSystemDiskSize(vmParams.getSystemDiskSize());
        createVmInfo.setImageId(vmParams.getImageId());
        createVmInfo.setDataDisks(vmParams.getDataDisks());
        createVmInfo.setInstanceType(createInfo.getInstanceType());
        createVmInfo.setZxAuth(type.getZxAuth());
        createVmInfo.setZoneId(clusterInfoParams.getZoneId());
        createVmInfo.setCurrentUser(loginUser);

        ComputeClusterCreateObjectInfo info = new ComputeClusterCreateObjectInfo();
        info.setComponent(type);
        info.setCreateVm(createVmInfo);
        info.setComponentId(clusterComponent.getId());
        info.setObjectType(SecurityClusterComponentObjectType.VM);
        return info;
    }

    private VmCreateInfo checkVmCreateInfo(CreateClusterVmParams vmParams, SecurityComponent securityComponent) throws GCloudException{

        VmCreateInfo info = new VmCreateInfo();

        Image image = imageDao.getById(vmParams.getImageId());

        if(image == null){
            throw new GCloudException("::image not exist");
        }

        //image 的tag还没有开发，后续增加
//        securityComponent.getTagType();




        InstanceType instanceType = instanceTypeDao.getById(vmParams.getInstanceType());
        if(instanceType == null){
            throw new GCloudException("::instance type not exist");
        }

        info.setInstanceType(instanceType);

        return info;
    }

    @Override
    public ComputeClusterCreateObjectInfo createClusterComponentDc(String clusterId, String clusterName, CurrentUser loginUser, CreateClusterInfoParams clusterInfoParams, CreateClusterDcParams dcParams, SecurityComponent type, Boolean isHa) {
        return null;
    }

    @Override
    public Map<SecurityClusterComponentObjectType, Map<String, CreateClusterObjectParams>> getComponentConfig(List<SecurityClusterComponent> components, CreateClusterInfoParams createInfo) {
        Map<SecurityClusterComponentObjectType, Map<String, CreateClusterObjectParams>> createObjectMap = new HashMap<>();

        Map<String, CreateClusterObjectParams> instanceMap = new HashMap<>();
//        Map<String, CreateClusterObjectParams> containerMap = new HashMap<>();
        createObjectMap.put(SecurityClusterComponentObjectType.VM, instanceMap);
//        createObjectMap.put(ClusterComponentObjectType.DOCKER_CONTAINER, containerMap);

        List<String> instanceIds = new ArrayList<>();
        List<String> containerIds = new ArrayList<>();

        Map<String, SecurityClusterComponent> componentObjectMap = new HashMap<>();

        for(SecurityClusterComponent component : components){

            SecurityComponent type = SecurityComponent.getByValue(component.getType());
            if(!type.getHasHa()){
                continue;
            }
            //没有虚拟机，组件有问题
            if(StringUtils.isBlank(component.getObjectId())){
                throw new GCloudException("security_controller_cluster_080011::component is in error");
            }

            SecurityClusterComponentObjectType objectType = SecurityClusterComponentObjectType.getByValue(component.getObjectType());
            if(SecurityClusterComponentObjectType.VM.equals(objectType)){
                instanceIds.add(component.getObjectId());
            }else if(SecurityClusterComponentObjectType.DOCKER_CONTAINER.equals(objectType)){
                containerIds.add(component.getObjectId());
            }

            componentObjectMap.put(component.getObjectId(), component);
        }

        if(instanceIds != null && instanceIds.size() > 0){

            List<VmInstance> instances = instanceDao.getByIds(instanceIds);

            for(VmInstance instance : instances){

                CreateClusterVmParams vmParams = new CreateClusterVmParams();
                vmParams.setInstanceType(instance.getInstanceType());
                vmParams.setImageId(instance.getImageId());


                List<VolumeAttachInfo> volumes = volumeDao.instanceVolume(instance.getId(), VolumeAttachInfo.class);
                List<DiskInfo> diskInfos = new ArrayList<>();
                String systemDiskCategory = null;
                Integer systemDiskSize = null;

                if(volumes != null && volumes.size() > 0){
                    for(VolumeAttachInfo volume : volumes){
                        if(Device.VDA.getMountPath().equals(volume.getMountpoint())){
                            systemDiskCategory = volume.getCategory();
                            systemDiskSize = volume.getSize();
                        }else{
                            DiskInfo diskInfo = new DiskInfo();
                            diskInfo.setZoneId(createInfo.getZoneId());
                            diskInfo.setSize(volume.getSize());
                            diskInfo.setCategory(volume.getCategory());

                            diskInfos.add(diskInfo);
                        }

                    }
                }

                if(systemDiskCategory == null || systemDiskSize == null){
                    throw new GCloudException("::获取云服务器系统盘信息失败");
                }

                vmParams.setSystemDiskCategory(systemDiskCategory);
                vmParams.setSystemDiskSize(systemDiskSize);
                vmParams.setDataDisks(diskInfos);

                CreateClusterObjectParams objParam = new CreateClusterObjectParams();
                objParam.setObjectType(SecurityClusterComponentObjectType.VM.value());
                objParam.setVm(vmParams);

                instanceMap.put(instance.getId(), objParam);
            }
        }


        return createObjectMap;
    }
}
