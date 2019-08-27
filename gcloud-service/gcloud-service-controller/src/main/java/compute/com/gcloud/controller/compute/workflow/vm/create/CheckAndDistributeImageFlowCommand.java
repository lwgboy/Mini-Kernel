package com.gcloud.controller.compute.workflow.vm.create;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.compute.workflow.model.vm.CheckAndDistributeImageFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.CheckAndDistributeImageFlowCommandRes;
import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.controller.image.enums.ImageDistributeTargetType;
import com.gcloud.controller.image.enums.ImageStorageType;
import com.gcloud.controller.image.enums.ImageStoreStatus;
import com.gcloud.controller.image.model.DistributeImageParams;
import com.gcloud.controller.image.prop.ImageProp;
import com.gcloud.controller.image.provider.IImageProvider;
import com.gcloud.controller.image.service.IImageStoreService;
import com.gcloud.controller.storage.dao.StoragePoolDao;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class CheckAndDistributeImageFlowCommand  extends BaseWorkFlowCommand{
	@Autowired
	IImageStoreService imageStoreService;
	
	@Autowired
	ImageProp imageProp;
	
	@Autowired
    private StoragePoolDao storagePoolDao;
	
	@Override
	public boolean judgeExecute() {
		CheckAndDistributeImageFlowCommandReq req = (CheckAndDistributeImageFlowCommandReq)getReqParams();
		
		StoragePool pool = this.storagePoolDao.checkAndGet(req.getZoneId(), req.getSystemDiskCategory());
		
		//需要skip的情况1、Glance 非local虚拟机；2、Gcloud rbd后端 rbd虚拟机；3、除上面两种情况外，store不为空；
		if(ResourceProviders.getDefault(ResourceType.IMAGE).providerType().equals(ProviderType.GLANCE)
			&& !pool.getStorageType().equals(StorageType.LOCAL.name().toLowerCase())) {
			return false;
		}
		if(ResourceProviders.getDefault(ResourceType.IMAGE).providerType().equals(ProviderType.GCLOUD) 
		&& imageProp.getStroageType().equals(ImageStorageType.RBD.value())
		&& pool.getStorageType().equals(StorageType.DISTRIBUTED.name().toLowerCase())) {
			return false;
		}
		
		Map<String, Object> props = new HashMap<String, Object>();
		if(pool.getStorageType().equals(StorageType.LOCAL.name().toLowerCase())) {
			props.put("storeTarget", req.getCreateHost());
		} else {
			props.put("storeTarget", pool.getPoolName());
		}
		
		props.put("imageId", req.getImageId());
		
		ImageStore store = imageStoreService.findUniqueByProperties(props);
		if(store != null && !store.getStatus().equals(ImageStoreStatus.ACTIVE.value())) {
			throw new GCloudException("::镜像在分发中，请等待分发完成后再创建");
		}
		
		return store == null;
	}
	
	@Override
	protected Object process() throws Exception {
		CheckAndDistributeImageFlowCommandReq req = (CheckAndDistributeImageFlowCommandReq)getReqParams();
		StoragePool pool = this.storagePoolDao.checkAndGet(req.getZoneId(), req.getSystemDiskCategory());
		String targetType = "";
		String target = "";
		if(pool.getStorageType().equals(StorageType.LOCAL.name().toLowerCase())) {
			targetType = ImageDistributeTargetType.NODE.value();
			target = req.getCreateHost();
		}else if(pool.getStorageType().equals(StorageType.DISTRIBUTED.name().toLowerCase())) {
			targetType = ImageDistributeTargetType.RBD.value();
			target = pool.getPoolName();
		} else {
			targetType = ImageDistributeTargetType.VG.value();
			target = pool.getPoolName();
		}
		
		DistributeImageParams params = new DistributeImageParams();
		params.setImageId(req.getImageId());
		params.setTarget(target);
		params.setTargetType(targetType);
		params.setTaskId(getTaskId());
		
		IImageProvider provider = ResourceProviders.getDefault(ResourceType.IMAGE);
		provider.distributeImage(params);
		//node是异步、vg\rbd是同步还是异步，如何处理？
		return null;
	}

	@Override
	protected Object rollback() throws Exception {
		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return CheckAndDistributeImageFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return CheckAndDistributeImageFlowCommandRes.class;
	}
	
	@Override
	public int getTimeOut() {
		//20分钟
		return 1200;
	}
}
