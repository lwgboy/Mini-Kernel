package com.gcloud.controller.network.handler.api.securitygroup;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.RevokeSecurityGroupMsg;
@GcLog(taskExpect="删除安全组规则成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.SECURITYGROUP,action="RevokeSecurityGroup")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SECURITYGROUPRULE, resourceIdField = "securityGroupRuleId")
public class ApiRevokeSecurityGroupHandler extends MessageHandler<RevokeSecurityGroupMsg, ApiReplyMessage> {
	@Autowired
	ISecurityGroupService securityGroupService;
	@Override
	public ApiReplyMessage handle(RevokeSecurityGroupMsg msg) throws GCloudException {
		securityGroupService.revokeSecurityGroup(msg.getSecurityGroupRuleId());
		
		msg.setObjectId(msg.getSecurityGroupRuleId());
		return new ApiReplyMessage();
	}

}
