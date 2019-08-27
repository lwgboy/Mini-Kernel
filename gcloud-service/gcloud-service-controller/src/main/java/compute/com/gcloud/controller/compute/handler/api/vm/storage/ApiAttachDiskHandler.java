package com.gcloud.controller.compute.handler.api.vm.storage;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.workflow.model.storage.AttachDataDiskInitFlowCommandRes;
import com.gcloud.controller.compute.workflow.model.storage.AttachDataDiskWorkflowReq;
import com.gcloud.controller.compute.workflow.vm.storage.AttachDataDiskWorkflow;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.storage.ApiAttachDiskMsg;
import com.gcloud.header.compute.msg.api.vm.storage.ApiAttachDiskReplyMsg;
import com.gcloud.header.log.model.Task;

import java.util.UUID;

@LongTask
@GcLog(isMultiLog = true, taskExpect = "挂载磁盘")
@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = "AttachDisk")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.VOLUME, resourceIdField = "diskId")
public class ApiAttachDiskHandler extends BaseWorkFlowHandler<ApiAttachDiskMsg, ApiAttachDiskReplyMsg> {

    @Override
    public ApiAttachDiskReplyMsg process(ApiAttachDiskMsg msg) throws GCloudException {
        ApiAttachDiskReplyMsg reply = new ApiAttachDiskReplyMsg();
        // 记录操作日志，任务流无论单操作还是批量 都用这种方式记录操作日志
        AttachDataDiskInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), AttachDataDiskInitFlowCommandRes.class);
        String except = String.format("挂载磁盘[%s]成功", res.getVolumeId());
        reply.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(res.getVolumeId()).objectName(res.getVolumeName()).expect(except).build());

        String requestId = UUID.randomUUID().toString();
        reply.setRequestId(requestId);
        reply.setSuccess(true);
        return reply;
    }

    @Override
    public Class getWorkflowClass() {
        return AttachDataDiskWorkflow.class;
    }

    @Override
    public Object preProcess(ApiAttachDiskMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public Object initParams(ApiAttachDiskMsg msg) {
        AttachDataDiskWorkflowReq req = new AttachDataDiskWorkflowReq();
        req.setInstanceId(msg.getInstanceId());
        req.setVolumeId(msg.getDiskId());
        req.setInTask(false);
        return req;
    }
}
