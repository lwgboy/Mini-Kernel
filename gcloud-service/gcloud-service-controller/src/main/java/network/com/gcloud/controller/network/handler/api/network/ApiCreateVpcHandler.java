package com.gcloud.controller.network.handler.api.network;


import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.IVpcService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.CreateVpcMsg;
import com.gcloud.header.network.msg.api.CreateVpcReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

@ApiHandler(module=Module.ECS,subModule=SubModule.VPC,action="CreateVpc")
public class ApiCreateVpcHandler extends MessageHandler<CreateVpcMsg, CreateVpcReplyMsg> {
	
	@Autowired
	IVpcService service;
	
	@Override
	public CreateVpcReplyMsg handle(CreateVpcMsg msg) throws GCloudException {
		// TODO Auto-generated method stub
		CreateVpcReplyMsg reply = new CreateVpcReplyMsg();
		String vpcId = service.createVpc(msg);
		reply.setVpcId(vpcId);
		return reply;
	}

}
