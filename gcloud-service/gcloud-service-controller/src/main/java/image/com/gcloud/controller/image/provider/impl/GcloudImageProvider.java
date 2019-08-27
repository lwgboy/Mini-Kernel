
package com.gcloud.controller.image.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.image.async.UploadGcloudImageAsync;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.dao.ImagePropertyDao;
import com.gcloud.controller.image.dao.ImageStoreDao;
import com.gcloud.controller.image.driver.ImageDriverEnum;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.entity.ImageProperty;
import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.controller.image.entity.enums.ImagePropertyItem;
import com.gcloud.controller.image.enums.ImageStoreStatus;
import com.gcloud.controller.image.model.CreateImageParams;
import com.gcloud.controller.image.model.DistributeImageParams;
import com.gcloud.controller.image.prop.ImageProp;
import com.gcloud.controller.image.provider.IImageProvider;
import com.gcloud.controller.image.workflow.DeleteGcloudImageWorkflow;
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
import com.gcloud.header.image.enums.ImageStatus;
import com.gcloud.header.image.msg.api.GenDownloadVo;
import com.gcloud.header.image.msg.node.DeleteImageMsg;
import com.gcloud.service.common.compute.model.QemuInfo;
import com.gcloud.service.common.compute.uitls.DiskQemuImgUtil;

import lombok.extern.slf4j.Slf4j;

import org.openstack4j.model.image.v2.DiskFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Component
public class GcloudImageProvider implements IImageProvider {
	@Autowired
    private ImageDao imageDao;

    @Autowired
    private ImagePropertyDao imagePropertyDao;
    
    @Autowired
    ImageProp prop;
    
    @Autowired
	private ImageStoreDao storeDao;
    
	@Autowired
	private MessageBus bus;
    
    @Override
    public ResourceType resourceType() {
        return ResourceType.IMAGE;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GCLOUD;
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

        long minDisk = (long)Math.ceil(info.getVirtualSize() / 1024.0 / 1024.0 / 1024.0);

        SimpleFlowChain<Long, String> chain = new SimpleFlowChain<>("create image");
        chain.then(new Flow<Long>() {
            @Override
            public void run(SimpleFlowChain chain, Long data) {

                chain.data(file.length());
                if(StringUtils.isBlank(params.getImageId())){
                    Image image = new Image();
                    image.setId(imageId);
                    image.setSize(file.length());
                    image.setMinDisk(minDisk);
                    image.setCreatedAt(new Date());
                    image.setName(params.getImageName());
                    //image.setStatus(ResourceStates.status(ResourceType.IMAGE, ProviderType.GCLOUD, ImageStatus.SAVING.value()));
                    image.setStatus(ImageStatus.SAVING.value());
                    image.setOwner(currentUser.getId());
                    image.setTenantId(currentUser.getDefaultTenant());
                    image.setProvider(providerType().getValue());
                    image.setProviderRefId(imageId);
                    //暂时默认都是公共
                    image.setOwnerType(ImageOwnerType.SYSTEM.value());
                    image.setDisable(false);
                    imageDao.save(image);

                    Map<String, String> properties = new HashMap<String, String>();
                    if (StringUtils.isNotBlank(params.getOsType())) {
                        properties.put(ImagePropertyItem.OS_TYPE.value(), params.getOsType());
                    }
                    if (StringUtils.isNotBlank(params.getDescription())) {
                        properties.put(ImagePropertyItem.DESCRIPTION.value(), params.getDescription());
                    }
                    if (StringUtils.isNotBlank(params.getArchitecture())) {
                        properties.put(ImagePropertyItem.ARCHITECTURE.value(), params.getArchitecture());
                    }
                    for(Map.Entry<String, String> entry : properties.entrySet()){
                        ImageProperty property = new ImageProperty();
                        property.setImageId(image.getId());
                        property.setName(entry.getKey());
                        property.setValue(entry.getValue());

                        imagePropertyDao.save(property);
                    }
                }else{

                    Image updateImage = new Image();
                    List<String> updateField = new ArrayList<>();
                    updateImage.setId(imageId);
                    updateField.add(updateImage.updateStatus(ImageStatus.SAVING.value()));
                    updateField.add(updateImage.updateUpdatedAt(new Date()));
                    updateField.add(updateImage.updateSize(file.length()));
                    updateField.add(updateImage.updateMinDisk(minDisk));
                    updateField.add(updateImage.updateProvider(providerType().getValue()));
                    updateField.add(updateImage.updateProviderRefId(imageId));

                    imageDao.update(updateImage, updateField);
                }

                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, Long data) {
                if(StringUtils.isBlank(params.getImageId())){
                    imageDao.deleteById(imageId);
                    imagePropertyDao.deleteByImageId(imageId);
                }
            }
        }).then(new NoRollbackFlow<Long>() {
            @Override
            public void run(SimpleFlowChain chain, Long data) {
            	//通过镜像文件大小来判断是否拷贝完整来判断可用状态
                UploadGcloudImageAsync async = new UploadGcloudImageAsync();
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
        // 只需要更改数据库属性
    	 List<String> updateField = new ArrayList<>();
         Image image = new Image();
         image.setId(imageId);
         updateField.add(image.updateName(imageName));
         imageDao.update(image, updateField);
    }

    @Override
    public void deleteImage(String imageId, String imageProviderRefId) throws GCloudException {
    	//把以下逻辑用任务流来实现
    	/*ImageDriverEnum.getByType(prop.getStroageType()).deleteImage(imageId);
    	
    	imageDao.deleteById(imageId);
        imagePropertyDao.deleteByImageId(imageId);
    	
    	//根据image_store删除各节点上、vg上、rbd上的对应的image cache   deleteImageCache
        List<ImageStore> stores = storeDao.findByProperty("imageId", imageId);
		for(ImageStore store:stores) {
			DeleteImageMsg deleteMsg = new DeleteImageMsg();
			String controllerService = MessageUtil.controllerServiceId();
			String controllerHost = controllerService.substring(controllerService.indexOf("-") + 1);
			deleteMsg.setServiceId(store.getStoreType().equals("node")?MessageUtil.imageServiceId(store.getStoreTarget()):MessageUtil.imageServiceId(controllerHost));
			deleteMsg.setImageId(imageId);
			deleteMsg.setStoreTarget(store.getStoreTarget());
			deleteMsg.setStoreType(store.getStoreType());
			deleteMsg.setTaskId(null);
	        
	        bus.send(deleteMsg);
		}
        
        //删除对应的image store info
    	storeDao.deleteByImageId(imageId);*/
    	log.debug("delete gcloud image....thread id:" + Thread.currentThread().getId());
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("imageId", imageId);
    	DeleteGcloudImageWorkflow deleteImageCacheFlow = new DeleteGcloudImageWorkflow();
    	deleteImageCacheFlow.setNeedFeedbackLog(false);
    	deleteImageCacheFlow.execute(params);
    }

    public List<Image> listImage(Map<String, String> filters) throws GCloudException {
        throw new GCloudException("no need to implement.");
    }

    @Override
    public GenDownloadVo genDownload(String imageId, String providerRefId) {
        return null;
    }
    
    @Override
    public void distributeImage(DistributeImageParams params) {
    	//记录image_store_info
		ImageStore store = new ImageStore();
		store.setImageId(params.getImageId());
		store.setStoreTarget(params.getTarget());
		store.setStoreType(params.getTargetType());
		store.setStatus(ImageStoreStatus.DOWNLOADING.value());
		
		storeDao.save(store);
    			
    	ImageDriverEnum.getByType(prop.getStroageType()).distributeImage(params.getImageId(), params.getTarget(), params.getTargetType(), params.getTaskId());
    }
    
}
