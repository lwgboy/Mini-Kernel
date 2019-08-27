package com.gcloud.controller.network.handler.api.router;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.SetVRouterGatewayMsg;
import org.springframework.beans.factory.annotation.Autowired;
@GcLog(taskExpect="设置路由器网关成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.VROUTER,action="SetVRouterGateway")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.ROUTER, resourceIdField = "vRouterId")
public class ApiSetVRouterGatewayHandler extends MessageHandler<SetVRouterGatewayMsg, ApiReplyMessage>{
	@Autowired
	IRouterService vRouterService;
	
	@Override
	public ApiReplyMessage handle(SetVRouterGatewayMsg msg) throws GCloudException {
		vRouterService.setVRouterGateway(msg.getvRouterId(), msg.getExternalNetworkId(), msg.getCurrentUser());
		
		msg.setObjectId(msg.getvRouterId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.ROUTER_NAME, msg.getvRouterId()));
		return new ApiReplyMessage();
	}

}
