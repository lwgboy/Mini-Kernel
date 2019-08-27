package com.gcloud.controller.compute.handler.node.vm.storage;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.vm.storage.CleanDataDiskConfigFileReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class CleanDataDiskFileReplyHandler extends AsyncMessageHandler<CleanDataDiskConfigFileReplyMsg> {

	@Override
	public void handle(CleanDataDiskConfigFileReplyMsg msg) {
		log.debug("CleanDataDiskFileReplyHandler" + msg.getSuccess());
	}

}
