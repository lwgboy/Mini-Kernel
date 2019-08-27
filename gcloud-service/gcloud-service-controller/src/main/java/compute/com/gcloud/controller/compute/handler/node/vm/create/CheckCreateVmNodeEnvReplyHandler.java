package com.gcloud.controller.compute.handler.node.vm.create;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.vm.create.CheckCreateVmNodeEnvReplyMsg;

import lombok.extern.slf4j.Slf4j;
@Handler
@Slf4j
public class CheckCreateVmNodeEnvReplyHandler  extends AsyncMessageHandler<CheckCreateVmNodeEnvReplyMsg>{
	@Override
	public void handle(CheckCreateVmNodeEnvReplyMsg msg) {
		log.debug( "CheckCreateVmNodeEnvReplyHandler" + msg.getSuccess());
	}

}
