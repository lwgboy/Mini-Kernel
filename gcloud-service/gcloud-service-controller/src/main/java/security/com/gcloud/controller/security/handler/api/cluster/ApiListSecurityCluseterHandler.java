package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.ApiListSecurityCluseterParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.model.SecurityClusterListType;
import com.gcloud.header.security.msg.api.cluster.ApiListSecurityCluseterMsg;
import com.gcloud.header.security.msg.api.cluster.ApiListSecurityCluseterReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "ApiListSecurityCluseter")
public class ApiListSecurityCluseterHandler extends MessageHandler<ApiListSecurityCluseterMsg, ApiListSecurityCluseterReplyMsg>{

	@Autowired
	private ISecurityClusterService securityClusterService;
	
	@Override
	public ApiListSecurityCluseterReplyMsg handle(ApiListSecurityCluseterMsg msg) throws GCloudException {
		ApiListSecurityCluseterParams params = BeanUtil.copyProperties(msg, ApiListSecurityCluseterParams.class);
		PageResult<SecurityClusterListType> response = securityClusterService.apiListSecurityCluseter(params, msg.getCurrentUser());
		
		ApiListSecurityCluseterReplyMsg reply = new ApiListSecurityCluseterReplyMsg();
		reply.init(response);
		return reply;
	}

}
