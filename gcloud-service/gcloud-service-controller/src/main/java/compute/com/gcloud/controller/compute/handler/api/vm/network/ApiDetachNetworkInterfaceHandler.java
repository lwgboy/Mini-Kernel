package com.gcloud.controller.compute.handler.api.vm.network;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.workflow.model.network.DetachPortInitFlowCommandRes;
import com.gcloud.controller.compute.workflow.model.network.DetachPortWorkflowReq;
import com.gcloud.controller.compute.workflow.vm.network.DetachPortWorkflow;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.network.ApiDetachNetworkInterfaceMsg;

/**
 * Created by yaowj on 2018/11/15.
 */
@LongTask
@GcLog(isMultiLog = true, taskExpect = "卸载网卡")
@ApiHandler(module= Module.ECS, subModule = SubModule.NETWORKINTERFACE, action="DetachNetworkInterface")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.PORT, resourceIdField = "networkInterfaceId")
public class ApiDetachNetworkInterfaceHandler extends BaseWorkFlowHandler<ApiDetachNetworkInterfaceMsg, ApiReplyMessage> {

    @Override
    public Object preProcess(ApiDetachNetworkInterfaceMsg msg) throws GCloudException {

        return null;
    }

    @Override
    public ApiReplyMessage process(ApiDetachNetworkInterfaceMsg msg) throws GCloudException {
        ApiReplyMessage replyMessage = new ApiReplyMessage();
        DetachPortInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), DetachPortInitFlowCommandRes.class);
        replyMessage.setTaskId(res.getTaskId());
        return replyMessage;
    }

    @Override
    public Class getWorkflowClass() {
        return DetachPortWorkflow.class;
    }

    @Override
    public Object initParams(ApiDetachNetworkInterfaceMsg msg) {
        DetachPortWorkflowReq req = new DetachPortWorkflowReq();
        req.setInstanceId(msg.getInstanceId());
        req.setNetworkInterfaceId(msg.getNetworkInterfaceId());
        req.setInTask(false);
        return req;
    }
}
