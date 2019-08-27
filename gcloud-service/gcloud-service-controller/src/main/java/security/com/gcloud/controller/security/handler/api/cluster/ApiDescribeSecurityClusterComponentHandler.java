package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.DescribeSecurityClusterComponentParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.model.SecurityClusterComponentType;
import com.gcloud.header.security.msg.api.cluster.ApiDescribeSecurityClusterComponentMsg;
import com.gcloud.header.security.msg.api.cluster.ApiDescribeSecurityClusterComponentReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "DescribeSecurityClusterComponent")
public class ApiDescribeSecurityClusterComponentHandler extends MessageHandler<ApiDescribeSecurityClusterComponentMsg, ApiDescribeSecurityClusterComponentReplyMsg> {

	@Autowired
	private ISecurityClusterService securityClusterService;
	
    @Override
    public ApiDescribeSecurityClusterComponentReplyMsg handle(ApiDescribeSecurityClusterComponentMsg msg) throws GCloudException {
    	DescribeSecurityClusterComponentParams params = BeanUtil.copyProperties(msg, DescribeSecurityClusterComponentParams.class);
    	PageResult<SecurityClusterComponentType> response = securityClusterService.describeSecurityClusterComponent(params, msg.getCurrentUser());
    	
    	ApiDescribeSecurityClusterComponentReplyMsg reply = new ApiDescribeSecurityClusterComponentReplyMsg();
    	reply.init(response);
        return reply;
    }
}
