package com.gcloud.controller.network.handler.api.securitygroup;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.network.model.CreateSecurityGroupParams;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.CreateSecurityGroupMsg;
import com.gcloud.header.network.msg.api.CreateSecurityGroupReplyMsg;
@GcLog(taskExpect="新建安全组成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.SECURITYGROUP,action="CreateSecurityGroup")
public class ApiCreateSecurityGroupHandler extends MessageHandler<CreateSecurityGroupMsg, CreateSecurityGroupReplyMsg> {
	@Autowired
	ISecurityGroupService securityGroupService;
	
	@Override
	public CreateSecurityGroupReplyMsg handle(CreateSecurityGroupMsg msg) throws GCloudException {
		CreateSecurityGroupParams params = BeanUtil.copyProperties(msg, CreateSecurityGroupParams.class);
		CreateSecurityGroupReplyMsg reply = new CreateSecurityGroupReplyMsg();
		reply.setSecurityGroupId(securityGroupService.createSecurityGroup(params, msg.getCurrentUser()));
		
		msg.setObjectName(params.getSecurityGroupName());
		
		return reply;
	}

}
