package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.SecurityClusterAddInstanceParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.msg.api.cluster.ApiSecurityClusterAddInstanceMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "SecurityClusterAddInstance")
public class ApiSecurityClusterAddInstanceHandler extends MessageHandler<ApiSecurityClusterAddInstanceMsg, ApiReplyMessage> {

	@Autowired
	private ISecurityClusterService securityClusterService;
	
    @Override
    public ApiReplyMessage handle(ApiSecurityClusterAddInstanceMsg msg) throws GCloudException {
    	SecurityClusterAddInstanceParams params = BeanUtil.copyProperties(msg, SecurityClusterAddInstanceParams.class);
    	securityClusterService.securityClusterAddInstance(params, msg.getCurrentUser());
    	
    	ApiReplyMessage reply = new ApiReplyMessage();
        return reply;
    }
}
