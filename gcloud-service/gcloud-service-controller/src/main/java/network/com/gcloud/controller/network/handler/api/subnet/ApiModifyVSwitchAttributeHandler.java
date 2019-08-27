package com.gcloud.controller.network.handler.api.subnet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.ISubnetService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.ModifyVSwitchAttributeMsg;

@GcLog(taskExpect="修改交换机属性成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.VSWITCH,action="ModifyVSwitchAttribute")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SUBNET, resourceIdField = "vSwitchId")
public class ApiModifyVSwitchAttributeHandler extends MessageHandler<ModifyVSwitchAttributeMsg, ApiReplyMessage> {
	@Autowired
	ISubnetService subnetService;
	
	@Override
	public ApiReplyMessage handle(ModifyVSwitchAttributeMsg msg) throws GCloudException {
		subnetService.modifyAttribute(msg.getvSwitchId(), msg.getvSwitchName());
		
		msg.setObjectId(msg.getvSwitchId());
		msg.setObjectName(msg.getvSwitchName());
		return new ApiReplyMessage();
	}

}
