package com.gcloud.controller.network.handler.api.securitygroup;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.model.AuthorizeSecurityGroupParams;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.AuthorizeSecurityGroupMsg;
@GcLog(taskExpect="创建安全组规则成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.SECURITYGROUP,action="AuthorizeSecurityGroup")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SECURITYGROUP, resourceIdField = "securityGroupId")
public class ApiAuthorizeSecurityGroupHandler extends MessageHandler<AuthorizeSecurityGroupMsg, ApiReplyMessage> {
	@Autowired
	ISecurityGroupService securityGroupService;
	
	@Override
	public ApiReplyMessage handle(AuthorizeSecurityGroupMsg msg) throws GCloudException {
		AuthorizeSecurityGroupParams params = BeanUtil.copyProperties(msg, AuthorizeSecurityGroupParams.class);
		securityGroupService.authorizeSecurityGroup(params, msg.getCurrentUser());
		
		msg.setObjectId(msg.getSecurityGroupId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.SECURITYGROUP_NAME, msg.getSecurityGroupId()));
		return new ApiReplyMessage();
	}

}
