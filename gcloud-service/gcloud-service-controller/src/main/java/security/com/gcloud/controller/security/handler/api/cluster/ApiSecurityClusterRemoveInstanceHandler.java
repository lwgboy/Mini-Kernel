package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.SecurityClusterRemoveInstanceParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.msg.api.cluster.ApiSecurityClusterRemoveInstanceMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "SecurityClusterRemoveInstance")
public class ApiSecurityClusterRemoveInstanceHandler extends MessageHandler<ApiSecurityClusterRemoveInstanceMsg, ApiReplyMessage> {

	@Autowired
	private ISecurityClusterService securityClusterService;
	
    @Override
    public ApiReplyMessage handle(ApiSecurityClusterRemoveInstanceMsg msg) throws GCloudException {
    	SecurityClusterRemoveInstanceParams params = BeanUtil.copyProperties(msg, SecurityClusterRemoveInstanceParams.class);
    	securityClusterService.securityClusterRemoveInstance(params, msg.getCurrentUser());
    	
    	ApiReplyMessage reply = new ApiReplyMessage();
        return reply;
    }
}
