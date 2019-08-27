package com.gcloud.controller.compute.handler.api.vm.create;

import com.gcloud.controller.compute.workflow.model.vm.CreateInstanceFlowInitCommandRes;
import com.gcloud.controller.compute.workflow.model.vm.CreateInstanceWorkflowReq;
import com.gcloud.controller.compute.workflow.vm.create.CreateInstanceWorkflow;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.create.ApiCreateInstanceMsg;
import com.gcloud.header.compute.msg.api.vm.create.ApiCreateInstanceReplyMsg;
import com.gcloud.header.log.model.Task;

import java.util.List;
import java.util.UUID;

@LongTask
@GcLog(isMultiLog = true, taskExpect = "创建云服务器")
@ApiHandler(module = Module.ECS, subModule = SubModule.VM, action = "CreateInstance")
public class ApiCreateInstanceHandler extends BaseWorkFlowHandler<ApiCreateInstanceMsg, ApiCreateInstanceReplyMsg> {

	@Override
	public ApiCreateInstanceReplyMsg process(ApiCreateInstanceMsg msg) throws GCloudException {
		ApiCreateInstanceReplyMsg reply = new ApiCreateInstanceReplyMsg();
		// 记录操作日志，任务流无论单操作还是批量 都用这种方式记录操作日志
		List<CreateInstanceFlowInitCommandRes> firstRess = getFlowTaskFirstStepRes(msg.getTaskId(), CreateInstanceFlowInitCommandRes.class);
		for (CreateInstanceFlowInitCommandRes res : firstRess) {
			String except = String.format("创建云服务器[%s]成功", res.getInstanceId());
			reply.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(res.getInstanceId()).objectName(res.getInstanceName()).expect(except).errorCode(res.getErrorMsg()).build());
			reply.setInstanceId(res.getInstanceId());
		}

		String requestId = UUID.randomUUID().toString();
		reply.setRequestId(requestId);
		reply.setSuccess(true);
		return reply;
	}

	@Override
	public Class getWorkflowClass() {
		return CreateInstanceWorkflow.class;
	}

	@Override
	public Object preProcess(ApiCreateInstanceMsg msg) throws GCloudException {
		return null;
	}

    @Override
    public Object initParams(ApiCreateInstanceMsg msg) {
        CreateInstanceWorkflowReq req = BeanUtil.copyProperties(msg, CreateInstanceWorkflowReq.class);
        req.setSubnetId(msg.getvSwitchId());
        req.setIpAddress(msg.getPrivateIpAddress());
        req.setInTask(false);
        return req;
    }
}
