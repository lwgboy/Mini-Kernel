package com.gcloud.controller.network.handler.api.securitygroup;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.model.ModifySecurityGroupAttributeParams;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.ModifySecurityGroupAttributeMsg;
@GcLog(taskExpect="修改安全组属性成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.SECURITYGROUP,action="ModifySecurityGroupAttribute")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SECURITYGROUP, resourceIdField = "securityGroupId")
public class ApiModifySecurityGroupAttributeHandler extends MessageHandler<ModifySecurityGroupAttributeMsg, ApiReplyMessage> {
	@Autowired
	ISecurityGroupService securityGroupService;
	
	@Override
	public ApiReplyMessage handle(ModifySecurityGroupAttributeMsg msg) throws GCloudException {
		ModifySecurityGroupAttributeParams params = BeanUtil.copyProperties(msg, ModifySecurityGroupAttributeParams.class);
		securityGroupService.modifySecurityGroupAttribute(params);
		
		msg.setObjectId(params.getSecurityGroupId());
		msg.setObjectName(params.getSecurityGroupName());
		return new ApiReplyMessage();
	}

}
