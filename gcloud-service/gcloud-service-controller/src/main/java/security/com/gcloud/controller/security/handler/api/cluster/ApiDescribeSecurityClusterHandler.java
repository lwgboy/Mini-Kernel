package com.gcloud.controller.security.handler.api.cluster;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.security.model.DescribeSecurityClusterParams;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.security.model.SecurityClusterType;
import com.gcloud.header.security.msg.api.cluster.ApiDescribeSecurityClusterMsg;
import com.gcloud.header.security.msg.api.cluster.ApiDescribeSecurityClusterReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "DescribeSecurityCluster")
public class ApiDescribeSecurityClusterHandler extends MessageHandler<ApiDescribeSecurityClusterMsg, ApiDescribeSecurityClusterReplyMsg> {

	@Autowired
	private ISecurityClusterService securityClusterService;
	
    @Override
    public ApiDescribeSecurityClusterReplyMsg handle(ApiDescribeSecurityClusterMsg msg) throws GCloudException {
    	DescribeSecurityClusterParams params = BeanUtil.copyProperties(msg, DescribeSecurityClusterParams.class);
    	PageResult<SecurityClusterType> response = securityClusterService.describeSecurityCluster(params, msg.getCurrentUser());
    	
    	ApiDescribeSecurityClusterReplyMsg reply = new ApiDescribeSecurityClusterReplyMsg();
    	reply.init(response);
        return reply;
    }
}
