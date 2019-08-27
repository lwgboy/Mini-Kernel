package com.gcloud.controller.network.handler.api.router;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.ModifyVRouterAttributeMsg;
@GcLog(taskExpect="修改路由器属性成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.VROUTER,action="ModifyVRouterAttribute")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.ROUTER, resourceIdField = "vRouterId")
public class ApiModifyVRouterAttributeHandler extends MessageHandler<ModifyVRouterAttributeMsg, ApiReplyMessage>{
	@Autowired
	IRouterService vRouterService;
	
	@Override
	public ApiReplyMessage handle(ModifyVRouterAttributeMsg msg) throws GCloudException {
		vRouterService.modifyVRouterAttribute(msg);
		
		msg.setObjectId(msg.getvRouterId());
		msg.setObjectName(msg.getvRouterName());
		return new ApiReplyMessage();
	}

}
