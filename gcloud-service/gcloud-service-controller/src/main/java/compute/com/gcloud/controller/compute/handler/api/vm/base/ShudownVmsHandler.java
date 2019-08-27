package com.gcloud.controller.compute.handler.api.vm.base;

import com.gcloud.controller.compute.workflow.model.vm.StopInstanceCommandRes;
import com.gcloud.controller.compute.workflow.vm.base.ShutdownVmsFlow;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiStopVmsMsg;
import com.gcloud.header.compute.msg.api.vm.base.ApiStopVmsReplyMsg;
import com.gcloud.header.log.model.Task;

import java.util.List;
import java.util.UUID;

@ApiHandler(module = Module.VM, subModule = SubModule.VM, action = "shutdown")
@LongTask
@GcLog(isMultiLog = true, taskExpect = "关闭云服务器")
public class ShudownVmsHandler extends BaseWorkFlowHandler<ApiStopVmsMsg, ApiStopVmsReplyMsg> {

    @Override
    public Object preProcess(ApiStopVmsMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public ApiStopVmsReplyMsg process(ApiStopVmsMsg msg) throws GCloudException {
        //记录操作日志，任务流无论单操作还是批量 都用这种方式记录操作日志
        List<StopInstanceCommandRes> firstRess = getFlowTaskFirstStepRes(msg.getTaskId(), StopInstanceCommandRes.class);

        for (StopInstanceCommandRes res : firstRess) {
            String except = String.format("关闭云服务器成功");
            msg.getTasks().add(Task.builder().taskId("").objectId("").objectName("").expect(except).build());
        }

        ApiStopVmsReplyMsg reply = new ApiStopVmsReplyMsg();
        reply.setRequestId(UUID.randomUUID().toString());

        return reply;
    }

    @Override
    public Class getWorkflowClass() {
        return ShutdownVmsFlow.class;
    }

}
