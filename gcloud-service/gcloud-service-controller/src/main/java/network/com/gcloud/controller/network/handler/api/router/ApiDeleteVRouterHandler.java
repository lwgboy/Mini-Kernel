package com.gcloud.controller.network.handler.api.router;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.entity.Router;
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
import com.gcloud.header.network.msg.api.DeleteVRouterMsg;
import org.springframework.beans.factory.annotation.Autowired;
@GcLog(taskExpect="删除路由成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.VROUTER,action="DeleteVRouter")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.ROUTER, resourceIdField = "vRouterId")
public class ApiDeleteVRouterHandler extends MessageHandler<DeleteVRouterMsg, ApiReplyMessage>{
	@Autowired
	IRouterService vRouterService;
	
	@Override
	public ApiReplyMessage handle(DeleteVRouterMsg msg) throws GCloudException {
		vRouterService.deleteVRouter(msg.getvRouterId());
		msg.setObjectId(msg.getvRouterId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.ROUTER_NAME, msg.getvRouterId()));
		return new ApiReplyMessage();
	}

}
