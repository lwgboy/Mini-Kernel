package com.gcloud.controller.compute.handler.node.vm.storage;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.vm.storage.ConfigDataDiskFileReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class ConfigDataDiskFileReplyHandler extends AsyncMessageHandler<ConfigDataDiskFileReplyMsg> {

	@Override
	public void handle(ConfigDataDiskFileReplyMsg msg) {
		log.debug("ConfigDataDiskFileReplyHandler" + msg.getSuccess());
	}

}
