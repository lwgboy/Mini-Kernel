package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.SecurityClusterDetailParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.model.SecurityClusterDetailResponse;
import com.gcloud.header.security.msg.api.cluster.ApiSecurityClusterDetailMsg;
import com.gcloud.header.security.msg.api.cluster.ApiSecurityClusterDetailReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "SecurityClusterDetail")
public class ApiSecurityClusterDetailHandler extends MessageHandler<ApiSecurityClusterDetailMsg, ApiSecurityClusterDetailReplyMsg> {

	@Autowired
	private ISecurityClusterService securityClusterService;
	
    @Override
    public ApiSecurityClusterDetailReplyMsg handle(ApiSecurityClusterDetailMsg msg) throws GCloudException {
    	SecurityClusterDetailParams params = BeanUtil.copyProperties(msg, SecurityClusterDetailParams.class);
    	SecurityClusterDetailResponse response = securityClusterService.securityClusterDetail(params, msg.getCurrentUser());
    	
    	ApiSecurityClusterDetailReplyMsg reply = new ApiSecurityClusterDetailReplyMsg();
    	reply.setSecurityClusterDetail(response);
        return reply;
    }
}
