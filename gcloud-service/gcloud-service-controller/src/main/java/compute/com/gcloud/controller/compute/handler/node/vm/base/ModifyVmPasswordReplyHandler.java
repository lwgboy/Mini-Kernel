package com.gcloud.controller.compute.handler.node.vm.base;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.vm.base.ModifyPasswordReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class ModifyVmPasswordReplyHandler extends AsyncMessageHandler<ModifyPasswordReplyMsg> {

	@Override
	public void handle(ModifyPasswordReplyMsg msg) {
		log.debug("ModifyVmPasswordReplyHandler" + msg.getSuccess());
	}

}
