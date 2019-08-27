package com.gcloud.controller.network.handler.api.network;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.IVpcService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.DeleteVpcMsg;
import org.springframework.beans.factory.annotation.Autowired;

@ApiHandler(module=Module.ECS,subModule=SubModule.VPC,action="DeleteVpc")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.ROUTER, resourceIdField = "vpcId")
public class ApiDeleteVpcHandler extends MessageHandler<DeleteVpcMsg, ApiReplyMessage> {
	
	@Autowired
	IVpcService service;
	
	@Override
	public ApiReplyMessage handle(DeleteVpcMsg msg) throws GCloudException {
		// TODO Auto-generated method stub
		service.removeVpc(msg.getVpcId());
		
		msg.setObjectId(msg.getVpcId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.ROUTER_NAME, msg.getVpcId()));
		return new ApiReplyMessage();
	}

}
