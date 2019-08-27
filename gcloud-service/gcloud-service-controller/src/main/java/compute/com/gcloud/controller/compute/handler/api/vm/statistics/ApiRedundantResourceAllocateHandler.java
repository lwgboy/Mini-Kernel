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
import com.gcloud.header.compute.msg.api.model.RedundantResourceAllocateItem;
import com.gcloud.header.compute.msg.api.vm.statistics.ApiInstancesStatisticsMsg;
import com.gcloud.header.compute.msg.api.vm.statistics.ApiInstancesStatisticsReplyMsg;
import com.gcloud.header.compute.msg.api.vm.statistics.ApiRedundantResourceAllocateMsg;
import com.gcloud.header.compute.msg.api.vm.statistics.ApiRedundantResourceAllocateReplyMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "RedundantResourceAllocate")
@GcLog(taskExpect = "剩余资源分配方案")
public class ApiRedundantResourceAllocateHandler extends MessageHandler<ApiRedundantResourceAllocateMsg, ApiRedundantResourceAllocateReplyMsg>{
	@Autowired
	private IVmStatisticsService vmStatisticsService;

	@Override
	public ApiRedundantResourceAllocateReplyMsg handle(ApiRedundantResourceAllocateMsg msg) throws GCloudException {
		List<RedundantResourceAllocateItem> response = vmStatisticsService.redundantResourceAllocate();
		ApiRedundantResourceAllocateReplyMsg replyMsg = new ApiRedundantResourceAllocateReplyMsg();
		replyMsg.init(response);
		return replyMsg;
	}
}
