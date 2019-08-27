package com.gcloud.controller.image.distribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.controller.image.driver.ImageDriverEnum;
import com.gcloud.controller.image.prop.ImageProp;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.image.msg.node.DeleteImageMsg;
import com.gcloud.header.image.msg.node.DownloadImageMsg;

@Component
public class FileToNodeDistributeImageImpl implements IDistributeImage {
	@Autowired
	private MessageBus bus;
	
	@Autowired
	ImageProp prop;

	@Override
	public void distributeImage(String imageId, String target, String taskId) {
		DownloadImageMsg downloadMsg = new DownloadImageMsg();
        downloadMsg.setServiceId(MessageUtil.imageServiceId(target));
        downloadMsg.setImageId(imageId);
        downloadMsg.setImagePath(prop.getImageFilesystemStoreDir() + imageId);
        downloadMsg.setProvider(ProviderType.GCLOUD.name().toLowerCase());
        downloadMsg.setStroageType(ImageDriverEnum.FILE.getStorageType());
        downloadMsg.setTaskId(taskId);
        
        bus.send(downloadMsg);
	}

	/*@Override
	public void deleteImageCache(String imageId, String target, String storeType, String taskId) {
		DeleteImageMsg deleteMsg = new DeleteImageMsg();
		deleteMsg.setServiceId(MessageUtil.imageServiceId(target));
		deleteMsg.setImageId(imageId);
		deleteMsg.setStoreTarget(target);
		deleteMsg.setStoreType(storeType);
		deleteMsg.setTaskId(taskId);
        
        bus.send(deleteMsg);
	}*/

}
