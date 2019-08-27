package com.gcloud.controller.compute.handler.node.vm.create;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.vm.create.CreateDomainReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class CreateDomainReplyHandler extends AsyncMessageHandler<CreateDomainReplyMsg> {

	@Override
	public void handle(CreateDomainReplyMsg msg) {
		log.debug("CreateDomainReplyHandler" + msg.getSuccess());
	}

}
