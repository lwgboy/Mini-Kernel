package com.gcloud.controller.network.handler.api.router;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.gcloud.header.network.msg.api.DetachVSwitchVRouterMsg;
@GcLog(taskExpect="解绑交换机到路由器成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.VROUTER,action="DetachVSwitchVRouter")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.ROUTER, resourceIdField = "vRouterId")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SUBNET, resourceIdField = "vSwitchId")
public class ApiDetachVSwitchVRouterHandler extends MessageHandler<DetachVSwitchVRouterMsg, ApiReplyMessage> {
	@Autowired
	IRouterService vRouterService;
	
	@Override
	public ApiReplyMessage handle(DetachVSwitchVRouterMsg msg) throws GCloudException {
		vRouterService.detachVSwitchVRouter(msg.getvRouterId(), msg.getvSwitchId());
		msg.setObjectId(msg.getvSwitchId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.SUBNET_NAME, msg.getvSwitchId()));
		return new ApiReplyMessage();
	}

}
