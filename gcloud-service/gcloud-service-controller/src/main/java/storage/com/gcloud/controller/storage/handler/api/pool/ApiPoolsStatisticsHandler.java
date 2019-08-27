package com.gcloud.controller.storage.handler.api.pool;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.pool.ApiPoolsStatisticsMsg;
import com.gcloud.header.storage.msg.api.pool.ApiPoolsStatisticsReplyMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.DISK,action = "PoolStatistics")
@GcLog(taskExpect = "存储池统计")
public class ApiPoolsStatisticsHandler extends MessageHandler<ApiPoolsStatisticsMsg, ApiPoolsStatisticsReplyMsg>{
	@Autowired
	private IStoragePoolService poolService;

	@Override
	public ApiPoolsStatisticsReplyMsg handle(ApiPoolsStatisticsMsg msg) throws GCloudException {
		ApiPoolsStatisticsReplyMsg reply = poolService.poolStatistics(msg.getPoolId());
		return reply;
	}
}
