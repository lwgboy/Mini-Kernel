package com.gcloud.compute.handler.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.compute.msg.node.vm.base.StartVmSyncMsg;
import com.gcloud.header.compute.msg.node.vm.base.StartVmSyncMsgReply;

import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class StartVmSyncHandler extends MessageHandler<StartVmSyncMsg, StartVmSyncMsgReply>{

	@Autowired
	private IVmBaseNodeService vmBaseNodeService;

	@Override
	public StartVmSyncMsgReply handle(StartVmSyncMsg msg) throws GCloudException {

		System.out.println("=======node==handle====begin");
		StartVmSyncMsgReply reply = new StartVmSyncMsgReply();
		try {
			Thread.sleep(3000L);
		}catch (Exception ex){

		}

//		vmBaseNodeService.startup(msg.getInstanceId());
		reply.setInstanceId(msg.getInstanceId());
		System.out.println("=======node==handle====end");
		return reply;
	}
}
