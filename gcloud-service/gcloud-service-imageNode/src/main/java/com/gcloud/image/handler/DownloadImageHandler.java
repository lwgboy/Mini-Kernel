package com.gcloud.image.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.image.msg.node.DownloadImageMsg;
import com.gcloud.image.service.IImageNodeService;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class DownloadImageHandler  extends AsyncMessageHandler<DownloadImageMsg>{
	@Autowired
	IImageNodeService imageService;
	@Override
	public void handle(DownloadImageMsg msg) {
		imageService.downloadImage(msg);
	}

}
