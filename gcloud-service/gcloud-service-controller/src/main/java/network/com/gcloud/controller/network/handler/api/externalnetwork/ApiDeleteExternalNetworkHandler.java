package com.gcloud.controller.network.handler.api.externalnetwork;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.INetworkService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.DeleteExternalNetworkMsg;
import org.springframework.beans.factory.annotation.Autowired;

@ApiHandler(module=Module.ECS, subModule=SubModule.NETWORK, action="DeleteExternalNetwork")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.NETWORK, resourceIdField = "networkId")
public class ApiDeleteExternalNetworkHandler extends MessageHandler<DeleteExternalNetworkMsg, ApiReplyMessage>{

	@Autowired
	INetworkService service;
	
	@Override
	public ApiReplyMessage handle(DeleteExternalNetworkMsg msg) throws GCloudException {
		service.removeNetwork(msg.getNetworkId());
		msg.setObjectId(msg.getNetworkId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.NETWORK_NAME, msg.getNetworkId()));
		return new ApiReplyMessage();
	}

}
