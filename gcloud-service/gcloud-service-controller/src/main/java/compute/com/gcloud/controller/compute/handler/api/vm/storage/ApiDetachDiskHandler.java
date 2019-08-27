package com.gcloud.controller.compute.handler.api.vm.storage;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.service.vm.storage.IVmDiskService;
import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskInitFlowCommandRes;
import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskWorkflowReq;
import com.gcloud.controller.compute.workflow.vm.storage.DetachDataDiskWorkflow;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.storage.ApiDetachDiskMsg;
import com.gcloud.header.compute.msg.api.vm.storage.ApiDetachDiskReplyMsg;
import com.gcloud.header.log.model.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@LongTask
@GcLog(isMultiLog = true, taskExpect = "卸载磁盘")
@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = "DetachDisk")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.VOLUME, resourceIdField = "diskId")
public class ApiDetachDiskHandler extends BaseWorkFlowHandler<ApiDetachDiskMsg, ApiDetachDiskReplyMsg> {

	@Autowired
	private IVmDiskService diskService;

	@Override
	public ApiDetachDiskReplyMsg process(ApiDetachDiskMsg msg) throws GCloudException {
		ApiDetachDiskReplyMsg reply = new ApiDetachDiskReplyMsg();
		// 记录操作日志，任务流无论单操作还是批量 都用这种方式记录操作日志
		DetachDataDiskInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), DetachDataDiskInitFlowCommandRes.class);
		String except = String.format("卸载磁盘[%s]成功", res.getVolumeId());
		reply.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(res.getVolumeId()).objectName(res.getVolumeName()).expect(except).build());

		String requestId = UUID.randomUUID().toString();
		reply.setRequestId(requestId);
		reply.setSuccess(true);
		return reply;
	}

	@Override
	public Class getWorkflowClass() {
		return DetachDataDiskWorkflow.class;
	}

	@Override
	public Object preProcess(ApiDetachDiskMsg msg) throws GCloudException {
		diskService.detachDataDiskApiCheck(msg.getDiskId());
		return null;
	}

	@Override
	public Object initParams(ApiDetachDiskMsg msg) {
        DetachDataDiskWorkflowReq req = new DetachDataDiskWorkflowReq();
        req.setInstanceId(msg.getInstanceId());
        req.setVolumeId(msg.getDiskId());
        req.setInTask(false);
		return req;
	}
}
