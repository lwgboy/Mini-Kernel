package com.gcloud.controller.network.handler.api.externalnetwork;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.INetworkService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.model.ExternalNetworkSetType;
import com.gcloud.header.network.msg.api.DescribeExternalNetworksMsg;
import com.gcloud.header.network.msg.api.DescribeExternalNetworksReplyMsg;

@ApiHandler(module=Module.ECS, subModule=SubModule.NETWORK, action="DescribeExternalNetworks")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.NETWORK, resourceIdField = "networkIds")
public class ApiDescribeExternalNetworksHandler extends MessageHandler<DescribeExternalNetworksMsg, DescribeExternalNetworksReplyMsg>{

	@Autowired
	INetworkService service;
	
	@Override
	public DescribeExternalNetworksReplyMsg handle(DescribeExternalNetworksMsg msg) throws GCloudException {
		// TODO Auto-generated method stub
		PageResult<ExternalNetworkSetType> response = service.describeNetworks(msg);
		DescribeExternalNetworksReplyMsg reply = new DescribeExternalNetworksReplyMsg();
        reply.init(response);
        return reply;
	}

}
