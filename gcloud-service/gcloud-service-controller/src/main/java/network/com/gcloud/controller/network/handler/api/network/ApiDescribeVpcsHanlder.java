package com.gcloud.controller.network.handler.api.network;

import com.gcloud.controller.ResourceIsolationCheck;

import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.IVpcService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.model.VpcsItemType;
import com.gcloud.header.network.msg.api.DescribeVpcsMsg;
import com.gcloud.header.network.msg.api.DescribeVpcsReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

@ApiHandler(module=Module.ECS,subModule=SubModule.VPC,action="DescribeVpcs")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.ROUTER, resourceIdField = "vpcIds")
public class ApiDescribeVpcsHanlder extends MessageHandler<DescribeVpcsMsg, DescribeVpcsReplyMsg>{
	
	@Autowired
	IVpcService service;
	
	@Override
	public DescribeVpcsReplyMsg handle(DescribeVpcsMsg msg) throws GCloudException {
		// TODO Auto-generated method stub

		PageResult<VpcsItemType> response = service.describeVpcs(msg);
		DescribeVpcsReplyMsg reply = new DescribeVpcsReplyMsg();
        reply.init(response);
        
		return reply;
	}

}
