package com.gcloud.controller.image.handler.node;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.controller.image.service.IImageStoreService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.image.msg.node.DeleteImageReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class DeleteImageReplyHandler extends AsyncMessageHandler<DeleteImageReplyMsg>{
	@Autowired
	IImageStoreService imageStoreService;
	
	@Override
	public void handle(DeleteImageReplyMsg msg) {
		//  删除image_store_info表对应记录状态
		log.info("DeleteImageReplyHandler start");
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("imageId", msg.getImageId());
		props.put("storeTarget", msg.getStoreTarget());
		ImageStore store = imageStoreService.findUniqueByProperties(props);
		imageStoreService.delete(store);
	}

}
