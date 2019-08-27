package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.ModifySecurityClusterParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.msg.api.cluster.ApiModifySecurityClusterMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "ModifySecurityCluster")
public class ApiModifySecurityClusterHandler extends MessageHandler<ApiModifySecurityClusterMsg, ApiReplyMessage> {

	@Autowired
	private ISecurityClusterService securityClusterService;
	
    @Override
    public ApiReplyMessage handle(ApiModifySecurityClusterMsg msg) throws GCloudException {
    	ModifySecurityClusterParams params = BeanUtil.copyProperties(msg, ModifySecurityClusterParams.class);
    	securityClusterService.modifySecurityCluster(params, msg.getCurrentUser());
    	
    	ApiReplyMessage reply = new ApiReplyMessage();
        return reply;
    }
}
