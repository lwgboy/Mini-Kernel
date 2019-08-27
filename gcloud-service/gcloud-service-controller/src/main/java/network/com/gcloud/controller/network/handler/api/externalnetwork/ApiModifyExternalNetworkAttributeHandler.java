package com.gcloud.controller.network.handler.api.externalnetwork;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.INetworkService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.ModifyExternalNetworkAttributeMsg;
import com.gcloud.header.network.msg.api.ModifyVpcAttributeMsg;

@ApiHandler(module=Module.ECS, subModule=SubModule.NETWORK, action="ModifyExternalNetworkAttribute")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.NETWORK, resourceIdField = "networkId")
public class ApiModifyExternalNetworkAttributeHandler extends MessageHandler<ModifyExternalNetworkAttributeMsg, ApiReplyMessage>{

	@Autowired
	private INetworkService service;
	
	@Override
	public ApiReplyMessage handle(ModifyExternalNetworkAttributeMsg msg) throws GCloudException {
		// TODO Auto-generated method stub
		ModifyVpcAttributeMsg vpcMsg = BeanUtil.copyProperties(msg, ModifyVpcAttributeMsg.class);
		vpcMsg.setVpcId(msg.getNetworkId());
		vpcMsg.setVpcName(msg.getNetworkName());
		service.updateNetwork(vpcMsg);
		
		msg.setObjectId(msg.getNetworkId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.NETWORK_NAME, msg.getNetworkId()));
		return new ApiReplyMessage();
	}

}
