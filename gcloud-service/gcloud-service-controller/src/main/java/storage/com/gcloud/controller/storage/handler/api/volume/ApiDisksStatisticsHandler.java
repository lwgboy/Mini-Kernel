package com.gcloud.controller.storage.handler.api.volume;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.volume.ApiDisksStatisticsMsg;
import com.gcloud.header.storage.msg.api.volume.ApiDisksStatisticsReplyMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.DISK,action = "DiskStatistics")
@GcLog(taskExpect = "磁盘统计")
public class ApiDisksStatisticsHandler extends MessageHandler<ApiDisksStatisticsMsg, ApiDisksStatisticsReplyMsg>{
	@Autowired
	private IVolumeService volumeService;

	@Override
	public ApiDisksStatisticsReplyMsg handle(ApiDisksStatisticsMsg msg) throws GCloudException {
		ApiDisksStatisticsReplyMsg reply = volumeService.diskStatistics();
		return reply;
	}
}
