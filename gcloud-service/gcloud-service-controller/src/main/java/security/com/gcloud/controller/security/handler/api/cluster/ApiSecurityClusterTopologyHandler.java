package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.SecurityClusterTopologyParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.model.SecurityClusterTopologyResponse;
import com.gcloud.header.security.msg.api.cluster.ApiSecurityClusterTopologyMsg;
import com.gcloud.header.security.msg.api.cluster.ApiSecurityClusterTopologyReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "SecurityClusterTopology")
public class ApiSecurityClusterTopologyHandler extends MessageHandler<ApiSecurityClusterTopologyMsg, ApiSecurityClusterTopologyReplyMsg>	{

	@Autowired
	private ISecurityClusterService securityClusterService;
	
	@Override
	public ApiSecurityClusterTopologyReplyMsg handle(ApiSecurityClusterTopologyMsg msg) throws GCloudException {
		SecurityClusterTopologyParams params = BeanUtil.copyProperties(msg, SecurityClusterTopologyParams.class);
		SecurityClusterTopologyResponse response = securityClusterService.securityClusterTopology(params, msg.getCurrentUser());
    	
    	ApiSecurityClusterTopologyReplyMsg reply = new ApiSecurityClusterTopologyReplyMsg();
    	reply.setSecurityClusterTopology(response);
        return reply;
	}

}
