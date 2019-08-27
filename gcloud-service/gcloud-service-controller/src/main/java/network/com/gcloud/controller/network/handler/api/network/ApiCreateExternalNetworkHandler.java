package com.gcloud.controller.network.handler.api.network;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.network.service.INetworkService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.CreateExternalNetworkMsg;
import com.gcloud.header.network.msg.api.CreateExternalNetworkReplyMsg;

@ApiHandler(module=Module.ECS, subModule=SubModule.VPC, action="CreateExternalNetwork")
public class ApiCreateExternalNetworkHandler extends MessageHandler<CreateExternalNetworkMsg, CreateExternalNetworkReplyMsg> {

	@Autowired
	INetworkService service;
	
	@Override
	public CreateExternalNetworkReplyMsg handle(CreateExternalNetworkMsg msg) throws GCloudException {
		// TODO Auto-generated method stub
		CreateExternalNetworkReplyMsg reply = new CreateExternalNetworkReplyMsg();
		String networkId = service.createExternalNetwork(msg);
		reply.setNetworkId(networkId);
		return reply;
	}

}
