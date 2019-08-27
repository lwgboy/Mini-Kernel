package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.SecurityClusterAddableInstanceParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.model.SecurityClusterInstanceType;
import com.gcloud.header.security.msg.api.cluster.ApiDescribeSecurityClusterAddableInstanceMsg;
import com.gcloud.header.security.msg.api.cluster.ApiDescribeSecurityClusterAddableInstanceReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "DescribeSecurityClusterAddableInstance")
public class ApiDescribeSecurityClusterAddableInstanceHandler extends MessageHandler<ApiDescribeSecurityClusterAddableInstanceMsg, ApiDescribeSecurityClusterAddableInstanceReplyMsg> {

	@Autowired
	private ISecurityClusterService securityClusterService;
	
    @Override
    public ApiDescribeSecurityClusterAddableInstanceReplyMsg handle(ApiDescribeSecurityClusterAddableInstanceMsg msg) throws GCloudException {
    	SecurityClusterAddableInstanceParams params = BeanUtil.copyProperties(msg, SecurityClusterAddableInstanceParams.class);
    	PageResult<SecurityClusterInstanceType> response = securityClusterService.describeSecurityClusterAddableInstance(params, msg.getCurrentUser());
    	
    	ApiDescribeSecurityClusterAddableInstanceReplyMsg reply = new ApiDescribeSecurityClusterAddableInstanceReplyMsg();
    	reply.init(response);
        return reply;
    }
}
