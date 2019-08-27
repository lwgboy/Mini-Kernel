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
import com.gcloud.header.network.msg.api.AttachVSwitchVRouterMsg;
import org.springframework.beans.factory.annotation.Autowired;
@GcLog(taskExpect="绑定交换机到路由器成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.VSWITCH,action="AttachVSwitchVRouter")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.ROUTER, resourceIdField = "vRouterId")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SUBNET, resourceIdField = "vSwitchId")
public class ApiAttachVSwitchVRouterHandler extends MessageHandler<AttachVSwitchVRouterMsg, ApiReplyMessage> {
	@Autowired
	IRouterService vRouterService;
	
	@Override
	public ApiReplyMessage handle(AttachVSwitchVRouterMsg msg) throws GCloudException {
		vRouterService.attachVSwitchVRouter(msg.getvRouterId(), msg.getvSwitchId(), msg.getCurrentUser());
		
		msg.setObjectId(msg.getvSwitchId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.SUBNET_NAME, msg.getvSwitchId()));
		return new ApiReplyMessage();
	}

}
