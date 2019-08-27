package com.gcloud.controller.compute.handler.api.vm.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.service.vm.statistics.IVmStatisticsService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsItemType;
import com.gcloud.header.compute.msg.api.vm.statistics.ApiInstancesStatisticsMsg;
import com.gcloud.header.compute.msg.api.vm.statistics.ApiInstancesStatisticsReplyMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "InstanceStatistics")
@GcLog(taskExpect = "云服务器统计")
public class ApiInstancesStatisticsHandler  extends MessageHandler<ApiInstancesStatisticsMsg, ApiInstancesStatisticsReplyMsg>{
	@Autowired
	private IVmStatisticsService vmStatisticsService;

	@Override
	public ApiInstancesStatisticsReplyMsg handle(ApiInstancesStatisticsMsg msg) throws GCloudException {
		List<InstanceStatisticsItemType> response = vmStatisticsService.instanceStatistics();
		ApiInstancesStatisticsReplyMsg replyMsg = new ApiInstancesStatisticsReplyMsg();
		replyMsg.init(response);
		replyMsg.setAllNum(response.stream().map(InstanceStatisticsItemType::getCountNum).reduce(0, Integer::sum));
		return replyMsg;
	}
}
