package com.gcloud.controller.network.handler.sdk.securitygroup;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.network.model.CreateSecurityGroupParams;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.sdk.SdkCreateTenantSecurityGroupMsg;
import com.gcloud.header.network.msg.sdk.SdkCreateTenantSecurityGroupReplyMsg;

@ApiHandler(module=Module.ECS,subModule=SubModule.SECURITYGROUP,action="SdkCreateSecurityGroup")
public class SdkCreateTenantSecurityGroupHandler extends MessageHandler<SdkCreateTenantSecurityGroupMsg, SdkCreateTenantSecurityGroupReplyMsg> {
	@Autowired
	ISecurityGroupService securityGroupService;

	@Override
	public SdkCreateTenantSecurityGroupReplyMsg handle(SdkCreateTenantSecurityGroupMsg msg) throws GCloudException {
		CreateSecurityGroupParams params = BeanUtil.copyProperties(msg, CreateSecurityGroupParams.class);
		params.setDefaultSg(true);
		SdkCreateTenantSecurityGroupReplyMsg reply = new SdkCreateTenantSecurityGroupReplyMsg();
		reply.setSecurityGroupId(securityGroupService.createSecurityGroup(params, msg.getCurrentUser()));
		
		msg.setObjectName(params.getSecurityGroupName());
		
		return reply;
	}

}
