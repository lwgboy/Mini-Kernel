package com.gcloud.controller.compute.handler.api.vm.trash;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.workflow.model.trash.DeleteInstanceInitFlowCommandRes;
import com.gcloud.controller.compute.workflow.model.trash.DeleteInstanceWorkflowReq;
import com.gcloud.controller.compute.workflow.vm.trash.DeleteInstanceWorkflow;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.trash.ApiDeleteInstanceMsg;
import com.gcloud.header.log.model.Task;


/**
 * Created by yaowj on 2018/12/3.
 */
@LongTask
@GcLog(isMultiLog = true, taskExpect = "删除实例")
@ApiHandler(module= Module.ECS, subModule = SubModule.VM, action="DeleteInstance")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
public class ApiDeleteInstanceHandler extends BaseWorkFlowHandler<ApiDeleteInstanceMsg, ApiReplyMessage> {

    @Override
    public Object preProcess(ApiDeleteInstanceMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public ApiReplyMessage process(ApiDeleteInstanceMsg msg) throws GCloudException {
        ApiReplyMessage replyMessage = new ApiReplyMessage();
        DeleteInstanceInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), DeleteInstanceInitFlowCommandRes.class);
        msg.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(msg.getInstanceId()).expect("删除实例").build());

        return replyMessage;
    }

    @Override
    public Class getWorkflowClass() {
        return DeleteInstanceWorkflow.class;
    }

    @Override
    public Object initParams(ApiDeleteInstanceMsg msg) {
        DeleteInstanceWorkflowReq req = new DeleteInstanceWorkflowReq();
        req.setInstanceId(msg.getInstanceId());
        req.setInTask(false);
        return req;
    }
}
