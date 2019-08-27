package com.gcloud.controller.network.handler.api.network;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.ApiRoutersStatisticsMsg;
import com.gcloud.header.network.msg.api.ApiRoutersStatisticsReplyMsg;
@ApiHandler(module = Module.ECS,subModule=SubModule.VPC,action = "VpcStatistics")
@GcLog(taskExpect = "vpc统计")
public class ApiVpcsStatisticsHandler  extends MessageHandler<ApiRoutersStatisticsMsg, ApiRoutersStatisticsReplyMsg>{
	@Autowired
	private IRouterService routerService;

	@Override
	public ApiRoutersStatisticsReplyMsg handle(ApiRoutersStatisticsMsg msg) throws GCloudException {
		ApiRoutersStatisticsReplyMsg replyMsg = new ApiRoutersStatisticsReplyMsg();
		replyMsg.setAllNum(routerService.routerStatistics());
		return replyMsg;
	}
}
