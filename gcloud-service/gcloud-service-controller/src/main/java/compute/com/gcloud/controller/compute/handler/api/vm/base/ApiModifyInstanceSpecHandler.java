package com.gcloud.controller.compute.handler.api.vm.base;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.workflow.model.vm.ModifyInstanceSpecInitFlowCommandRes;
import com.gcloud.controller.compute.workflow.vm.base.ModifyInstanceSpecWorkflow;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiModifyInstanceSpecMsg;
import com.gcloud.header.compute.msg.api.vm.base.ApiModifyInstanceSpecReplyMsg;
import com.gcloud.header.log.model.Task;

@ApiHandler(module = Module.ECS, subModule = SubModule.VM, action = "ModifyInstanceSpec")
@LongTask
@GcLog(taskExpect = "修改实例规格")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
public class ApiModifyInstanceSpecHandler extends BaseWorkFlowHandler<ApiModifyInstanceSpecMsg, ApiModifyInstanceSpecReplyMsg> {

	@Override
	public Class getWorkflowClass() {
		return ModifyInstanceSpecWorkflow.class;
	}

	@Override
	public Object preProcess(ApiModifyInstanceSpecMsg msg) throws GCloudException {
		return null;
	}

	@Override
	public ApiModifyInstanceSpecReplyMsg process(ApiModifyInstanceSpecMsg msg) throws GCloudException {
		ApiModifyInstanceSpecReplyMsg replyMessage = new ApiModifyInstanceSpecReplyMsg();
		ModifyInstanceSpecInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), ModifyInstanceSpecInitFlowCommandRes.class);

		msg.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(msg.getInstanceId()).expect("修改实例规格").build());

		replyMessage.setTaskId(res.getTaskId());
		return replyMessage;
	}
}
