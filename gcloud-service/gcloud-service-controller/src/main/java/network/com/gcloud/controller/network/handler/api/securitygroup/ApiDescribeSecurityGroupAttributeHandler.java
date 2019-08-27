package com.gcloud.controller.network.handler.api.securitygroup;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.model.DescribeSecurityGroupAttributeResponse;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.DescribeSecurityGroupAttributeMsg;
import com.gcloud.header.network.msg.api.DescribeSecurityGroupAttributeReplyMsg;

@ApiHandler(module=Module.ECS,subModule=SubModule.SECURITYGROUP,action="DescribeSecurityGroupAttribute")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SECURITYGROUP, resourceIdField = "securityGroupId")
public class ApiDescribeSecurityGroupAttributeHandler extends MessageHandler<DescribeSecurityGroupAttributeMsg, DescribeSecurityGroupAttributeReplyMsg> {
	@Autowired
	ISecurityGroupService securityGroupService;
	
	@Override
	public DescribeSecurityGroupAttributeReplyMsg handle(DescribeSecurityGroupAttributeMsg msg) throws GCloudException {
		DescribeSecurityGroupAttributeResponse res = securityGroupService.describeSecurityGroupAttribute(msg.getSecurityGroupId(), msg.getDirection(), msg.getRegion());
		return BeanUtil.copyProperties(res, DescribeSecurityGroupAttributeReplyMsg.class);
	}

}
