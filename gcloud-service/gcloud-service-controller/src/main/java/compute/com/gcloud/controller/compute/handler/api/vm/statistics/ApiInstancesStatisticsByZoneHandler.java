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
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsZoneItemType;
import com.gcloud.header.compute.msg.api.vm.statistics.ApiInstancesStatisticsByZoneMsg;
import com.gcloud.header.compute.msg.api.vm.statistics.ApiInstancesStatisticsByZoneReplyMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "InstanceStatisticsByZone")
@GcLog(taskExpect = "可用区云服务器统计")
public class ApiInstancesStatisticsByZoneHandler extends MessageHandler<ApiInstancesStatisticsByZoneMsg, ApiInstancesStatisticsByZoneReplyMsg>{
	@Autowired
	private IVmStatisticsService vmStatisticsService;

	@Override
	public ApiInstancesStatisticsByZoneReplyMsg handle(ApiInstancesStatisticsByZoneMsg msg) throws GCloudException {
		List<InstanceStatisticsZoneItemType> response = vmStatisticsService.instanceStatisticsByZone();
		ApiInstancesStatisticsByZoneReplyMsg replyMsg = new ApiInstancesStatisticsByZoneReplyMsg();
		replyMsg.init(response);
		return replyMsg;
	}
}
