package com.gcloud.controller.image.handler.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.controller.image.enums.ImageStoreStatus;
import com.gcloud.controller.image.service.IImageStoreService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.image.msg.node.DownloadImageReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class DownloadImageReplyHandler extends AsyncMessageHandler<DownloadImageReplyMsg>{
	@Autowired
	IImageStoreService imageStoreService;
	
	@Override
	public void handle(DownloadImageReplyMsg msg) {
		log.info("DownloadImageReplyHandler start");
		// 修改image_store_info表对应记录状态
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("imageId", msg.getImageId());
		props.put("storeTarget", msg.getStoreTarget());
		ImageStore store = imageStoreService.findUniqueByProperties(props);
		if(msg.getSuccess()) {
			List<String> updateFields = new ArrayList<String>();
			updateFields.add(store.updateStatus(ImageStoreStatus.ACTIVE.value()));
			
			imageStoreService.update(store, updateFields);
		} else {
			imageStoreService.delete(store);
		}
	}

}
