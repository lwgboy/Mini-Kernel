package com.gcloud.controller.network.handler.api.router;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.CreateVRouterMsg;
import com.gcloud.header.network.msg.api.CreateVRouterReplyMsg;
@GcLog(taskExpect="创建路由成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.VROUTER,action="CreateVRouter")
public class ApiCreateVRouterHandler extends MessageHandler<CreateVRouterMsg, CreateVRouterReplyMsg> {
	@Autowired
	IRouterService vRouterService;
	
	@Override
	public CreateVRouterReplyMsg handle(CreateVRouterMsg msg) throws GCloudException {
		CreateVRouterReplyMsg reply=new CreateVRouterReplyMsg();
		reply.setvRouterId(vRouterService.createVRouter(msg));
		msg.setObjectId(reply.getvRouterId());
		msg.setObjectName(msg.getvRouterName());
		return reply;
	}

}
