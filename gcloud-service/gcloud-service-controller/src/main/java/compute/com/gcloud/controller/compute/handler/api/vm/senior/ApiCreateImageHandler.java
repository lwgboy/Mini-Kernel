package com.gcloud.controller.compute.handler.api.vm.senior;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.workflow.model.senior.BundleInstanceInitFlowCommandRes;
import com.gcloud.controller.compute.workflow.model.senior.BundleInstanceWorkflowReq;
import com.gcloud.controller.compute.workflow.vm.senior.bundle.BundleInstanceWorkflow;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.senior.ApiCreateImageMsg;
import com.gcloud.header.compute.msg.api.vm.senior.ApiCreateImageReplyMsg;
import com.gcloud.header.log.model.Task;

/**
 * Created by yaowj on 2018/9/17.
 */
@ApiHandler(module = Module.ECS, subModule=SubModule.VM, action = "CreateImage")
@LongTask
@GcLog(isMultiLog = true, taskExpect = "云服务器打包")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
public class ApiCreateImageHandler extends BaseWorkFlowHandler<ApiCreateImageMsg, ApiCreateImageReplyMsg> {

    @Override
    public Object preProcess(ApiCreateImageMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public ApiCreateImageReplyMsg process(ApiCreateImageMsg msg) throws GCloudException {

        ApiCreateImageReplyMsg replyMessage = new ApiCreateImageReplyMsg();
        BundleInstanceInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), BundleInstanceInitFlowCommandRes.class);
        msg.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(msg.getInstanceId()).expect("云服务器打包成功").build());
        replyMessage.setImageId(res.getImageId());
        replyMessage.setImageName(res.getImageName());

        return replyMessage;
    }

    @Override
    public Object initParams(ApiCreateImageMsg msg) {
        BundleInstanceWorkflowReq req = new BundleInstanceWorkflowReq();
        req.setInstanceId(msg.getInstanceId());
        req.setImageName(msg.getImageName());
        req.setInTask(false);
        req.setCurrentUser(msg.getCurrentUser());
        return req;
    }

    @Override
    public Class getWorkflowClass() {
        return BundleInstanceWorkflow.class;
    }
}
