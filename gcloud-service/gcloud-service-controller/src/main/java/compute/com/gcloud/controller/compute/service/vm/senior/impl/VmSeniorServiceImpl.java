package com.gcloud.controller.compute.service.vm.senior.impl;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dao.InstanceTypeDao;
import com.gcloud.controller.compute.entity.InstanceType;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.model.vm.VmBundleResponse;
import com.gcloud.controller.compute.service.vm.senior.IVmSeniorService;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.dao.ImagePropertyDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.entity.ImageProperty;
import com.gcloud.controller.image.entity.enums.ImagePropertyItem;
import com.gcloud.controller.image.service.IImageService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.ImageOwnerType;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.enums.VmTaskState;
import com.gcloud.header.image.enums.ImageStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yaowj on 2018/12/3.
 */
@Service
@Slf4j
@Transactional
public class VmSeniorServiceImpl implements IVmSeniorService {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private InstanceTypeDao instanceTypeDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private IImageService imageService;

    @Autowired
    private ImagePropertyDao imagePropertyDao;

    @Override
    public VmBundleResponse bundle(String instanceId, String imageName, boolean inTask, CurrentUser currentUser) {

        VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0011202::云服务器不能存在");
        }

        if(!VmState.STOPPED.value().equals(vm.getState())){
            throw new GCloudException("0011205::请先关闭云服务器");
        }

        if(!inTask){
            if (!instanceDao.updateInstanceTaskState(instanceId, VmTaskState.BUNDLING)) {
                throw new GCloudException("0011203::云服务器当前状态不能创建镜像");
            }
        }

        String imageId = UUID.randomUUID().toString();

        Image image = new Image();
        image.setId(imageId);
        image.setCreatedAt(new Date());
        image.setName(imageName);
        image.setMinDisk(0L);
        image.setStatus(ImageStatus.QUEUED.value());
        image.setOwner(currentUser.getId());
        image.setTenantId(currentUser.getDefaultTenant());

        //暂时默认都是公共
        image.setOwnerType(ImageOwnerType.SYSTEM.value());
        image.setDisable(false);
        imageDao.save(image);

        Map<String, String> imageProperties = imageService.getImageProperties(vm.getImageId());
        if(imageProperties != null){

            for(Map.Entry<String, String> props : imageProperties.entrySet()){
                if(ImagePropertyItem.ARCHITECTURE.value().equals(props.getKey()) || ImagePropertyItem.OS_TYPE.value().equals(props.getKey())){
                    ImageProperty property = new ImageProperty();
                    property.setImageId(image.getId());
                    property.setName(props.getKey());
                    property.setValue(props.getValue());

                    imagePropertyDao.save(property);
                }

            }

        }

        VmBundleResponse response = new VmBundleResponse();
        response.setImageId(imageId);
        response.setImageName(imageName);

        return response;
    }

    @Override
    public void modifyInstanceInit(String instanceId, String instanceType, boolean inTask) {
        VmInstance instance = instanceDao.getById(instanceId);
        if (instance == null) {
            throw new GCloudException("0011303::云服务器不存在");
        }

        InstanceType insType = instanceTypeDao.getById(instanceType);
        if (insType == null) {
            throw new GCloudException("0011304::实例类型不存在");
        }

        //关机才能修改配置，功能更加稳定，否则如果虚拟机无法通过libvirt命令关机，则会超时
        //关机才能配置，所以不需要处理资源
        if(!VmState.STOPPED.value().equals(instance.getState())){
            throw new GCloudException("0011305::请先关闭云服务器");
        }

        if(!inTask){
            if (!instanceDao.updateInstanceTaskState(instanceId, VmTaskState.MODIFYING_CONFIG)) {
                throw new GCloudException("0011306::云服务器当前状态不能修改实例规格");
            }
        }
    }
}
