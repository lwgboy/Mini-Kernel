
package com.gcloud.controller.image.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.image.async.UploadGlanceImageAsync;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.dao.ImagePropertyDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.entity.ImageProperty;
import com.gcloud.controller.image.entity.enums.ImagePropertyItem;
import com.gcloud.controller.image.model.CreateImageParams;
import com.gcloud.controller.image.model.DistributeImageParams;
import com.gcloud.controller.image.provider.IImageProvider;
import com.gcloud.controller.provider.GlanceProviderProxy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.ImageOwnerType;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.image.msg.api.GenDownloadVo;
import com.gcloud.header.image.msg.node.DownloadImageMsg;
import com.gcloud.service.common.compute.model.QemuInfo;
import com.gcloud.service.common.compute.uitls.DiskQemuImgUtil;
import org.openstack4j.api.Builders;
import org.openstack4j.model.image.v2.ContainerFormat;
import org.openstack4j.model.image.v2.DiskFormat;
import org.openstack4j.model.image.v2.builder.ImageBuilder;
import org.openstack4j.openstack.image.v2.domain.PatchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import org.openstack4j.model.image.v2.Image;

@Component
public class GlanceImageProvider implements IImageProvider {

    @Autowired
    private GlanceProviderProxy glanceProviderProxy;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImagePropertyDao imagePropertyDao;
    
    @Autowired
	private MessageBus bus;
	
    @Override
    public ResourceType resourceType() {
        return ResourceType.IMAGE;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GLANCE;
    }

    @Override
    public String createImage(CreateImageParams params, CurrentUser currentUser) throws GCloudException {

        File file = new File(params.getFilePath());
        if (!file.exists()) {
            throw new GCloudException("0090106::镜像文件不存在");
        }

        String imageId = StringUtils.isBlank(params.getImageId()) ? UUID.randomUUID().toString() : params.getImageId();

        QemuInfo info = DiskQemuImgUtil.info(params.getFilePath());
        DiskFormat diskFormat = DiskFormat.value(info.getFormat());


        long minDisk = info.virtualSizeGb();

        SimpleFlowChain<org.openstack4j.model.image.Image, String> chain = new SimpleFlowChain<>("create image");
        chain.then(new Flow<org.openstack4j.model.image.v2.Image>() {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.image.v2.Image data) {
                ImageBuilder builder = Builders.imageV2().name(params.getImageName()).containerFormat(ContainerFormat.BARE).diskFormat(diskFormat);

                if (StringUtils.isNotBlank(params.getArchitecture())) {
                    builder.architecture(params.getArchitecture());
                }

                if (StringUtils.isNotBlank(params.getOsType())) {
                    builder.additionalProperty(ImagePropertyItem.OS_TYPE.value(), params.getOsType());
                }

                if (StringUtils.isNotBlank(params.getDescription())) {
                    builder.additionalProperty(ImagePropertyItem.DESCRIPTION.value(), params.getDescription());
                }

                builder.minDisk(minDisk);
                org.openstack4j.model.image.v2.Image image = glanceProviderProxy.createImage(builder.build());
                chain.data(image);
                chain.next();

            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.image.v2.Image data) {
                glanceProviderProxy.deleteImage(data.getId());
            }
        }).then(new Flow<org.openstack4j.model.image.v2.Image>() {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.image.v2.Image data) {

                if(StringUtils.isBlank(params.getImageId())){
                    Image image = new Image();
                    image.setId(imageId);
                    image.setMinDisk(minDisk);
                    image.setCreatedAt(data.getCreatedAt());
                    image.setName(data.getName());
                    image.setStatus(ResourceStates.status(ResourceType.IMAGE, ProviderType.GLANCE, data.getStatus().value()));
                    image.setOwner(currentUser.getId());
                    image.setTenantId(currentUser.getDefaultTenant());
                    image.setProvider(providerType().getValue());
                    image.setProviderRefId(data.getId());
                    //暂时默认都是公共
                    image.setOwnerType(ImageOwnerType.SYSTEM.value());
                    image.setDisable(false);
                    imageDao.save(image);

                    Map<String, String> properties = data.getAdditionalProperties();
                    if (properties != null && properties.size() > 0) {

                        Map<String, ImagePropertyItem> propItemMap = new HashMap<>();
                        Arrays.stream(ImagePropertyItem.values()).forEach(t -> propItemMap.put(t.value(), t));

                        for (Map.Entry<String, String> prop : properties.entrySet()) {

                            if(propItemMap.get(prop.getValue()) != null){
                                ImageProperty property = new ImageProperty();
                                property.setImageId(image.getId());
                                property.setName(prop.getKey());
                                property.setValue(prop.getValue());

                                imagePropertyDao.save(property);
                            }

                        }
                    }

                    //架构特殊处理，旧版的openstack AdditionalProperties 没有返回
                    if (StringUtils.isBlank(properties.get(ImagePropertyItem.ARCHITECTURE.value())) && StringUtils.isNotBlank(data.getArchitecture())) {
                        ImageProperty property = new ImageProperty();
                        property.setImageId(image.getId());
                        property.setName(ImagePropertyItem.ARCHITECTURE.value());
                        property.setValue(data.getArchitecture());

                        imagePropertyDao.save(property);
                    }

                }else{

                    Image updateImage = new Image();
                    List<String> updateField = new ArrayList<>();
                    updateImage.setId(imageId);
                    updateField.add(updateImage.updateStatus(ResourceStates.status(ResourceType.IMAGE, ProviderType.GLANCE, data.getStatus().value())));
                    updateField.add(updateImage.updateUpdatedAt(new Date()));
                    updateField.add(updateImage.updateMinDisk(minDisk));
                    updateField.add(updateImage.updateProvider(providerType().getValue()));
                    updateField.add(updateImage.updateProviderRefId(data.getId()));

                    imageDao.update(updateImage, updateField);

                }



                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.image.v2.Image data) {
                if(StringUtils.isBlank(params.getImageId())){
                    imageDao.deleteById(data.getId());
                    imagePropertyDao.deleteByImageId(data.getId());
                }
            }
        }).then(new NoRollbackFlow<org.openstack4j.model.image.v2.Image>() {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.image.v2.Image data) {
                UploadGlanceImageAsync async = new UploadGlanceImageAsync();
                async.setImage(data);
                async.setImageId(imageId);
                async.setFilePath(params.getFilePath());
                async.setTaskId(params.getTaskId());
                async.start();
                chain.next();
            }
        }).start();

        if (chain.getErrorCode() != null) {
            throw new GCloudException(chain.getErrorCode());
        }

        return imageId;
    }

    @Override
    public void updateImage(String imageId, String imageProviderRefId, String imageName) throws GCloudException {

        List<String> updateField = new ArrayList<>();
        Image image = new Image();
        image.setId(imageId);
        updateField.add(image.updateName(imageName));
        imageDao.update(image, updateField);

        PatchOperation patchOperation = new PatchOperation(PatchOperation.OperationType.REPLACE, "/name", imageName);
        glanceProviderProxy.updateImage(imageProviderRefId, patchOperation);

    }

    @Override
    public void deleteImage(String imageId, String imageProviderRefId) throws GCloudException {

        imageDao.deleteById(imageId);
        imagePropertyDao.deleteByImageId(imageId);

        glanceProviderProxy.deleteImage(imageProviderRefId);

    }

    public List<Image> listImage(Map<String, String> filters) throws GCloudException {
        List<org.openstack4j.model.image.v2.Image> imageList = glanceProviderProxy.listImage(filters);
        List<Image> list = new ArrayList<>();
        for (org.openstack4j.model.image.v2.Image i : imageList) {
            Image image = new Image();
//            image.setId(i.getId());
            image.setProvider(providerType().getValue());
            image.setProviderRefId(i.getId());
            image.setName(i.getName());
            image.setSize(i.getSize());
            image.setStatus(ResourceStates.status(ResourceType.IMAGE, ProviderType.GLANCE, i.getStatus().value()));
            image.setCreatedAt(i.getCreatedAt());
            image.setUpdatedAt(i.getUpdatedAt());
            image.setMinDisk(i.getMinDisk());
            image.setOwner(i.getOwner());
//            image.setOwnerType(i.getVisibility().value());
            image.setOwnerType("shared");

            list.add(image);
        }

        return list;
    }

    @Override
    public GenDownloadVo genDownload(String imageId, String providerRefId) {
        GenDownloadVo vo = new GenDownloadVo();
        vo.setImageId(imageId);
        vo.setImageRefId(providerRefId);
        vo.setTokenId(glanceProviderProxy.getClient().getToken().getId());
        vo.setServiceUrl(glanceProviderProxy.getUrl());
        return vo;
    }
    
    @Override
    public void distributeImage(DistributeImageParams params) {
    	DownloadImageMsg downloadMsg = new DownloadImageMsg();
        downloadMsg.setServiceId(MessageUtil.imageServiceId(params.getTarget()));
        downloadMsg.setImageId(params.getImageId());
        downloadMsg.setProvider(ProviderType.GLANCE.name().toLowerCase());
        downloadMsg.setTaskId(params.getTaskId());
        
        bus.send(downloadMsg);
    }
}
