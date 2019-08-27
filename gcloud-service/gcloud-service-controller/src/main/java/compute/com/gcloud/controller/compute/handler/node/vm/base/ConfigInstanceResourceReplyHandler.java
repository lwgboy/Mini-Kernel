package com.gcloud.controller.compute.handler.node.vm.base;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.vm.base.ConfigInstanceResourceReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class ConfigInstanceResourceReplyHandler extends AsyncMessageHandler<ConfigInstanceResourceReplyMsg> {

	@Override
	public void handle(ConfigInstanceResourceReplyMsg msg) {
		log.debug("ConfigInstanceResourceReplyHandler" + msg.getSuccess());
	}

}
