package com.gcloud.image.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.image.msg.node.DeleteImageMsg;
import com.gcloud.image.service.IImageNodeService;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class DeleteImageHandler  extends AsyncMessageHandler<DeleteImageMsg>{
	@Autowired
	IImageNodeService imageService;
	@Override
	public void handle(DeleteImageMsg msg) {
		imageService.deleteImage(msg);
	}

}
