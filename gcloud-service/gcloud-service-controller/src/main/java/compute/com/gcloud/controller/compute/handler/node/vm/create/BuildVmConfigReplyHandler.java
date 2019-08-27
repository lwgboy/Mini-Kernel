package com.gcloud.controller.compute.handler.node.vm.create;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.vm.create.BuildVmConfigReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class BuildVmConfigReplyHandler extends AsyncMessageHandler<BuildVmConfigReplyMsg> {

	@Override
	public void handle(BuildVmConfigReplyMsg msg) {
		log.debug( "BuildVmConfigReplyHandler" + msg.getSuccess());
	}

}
