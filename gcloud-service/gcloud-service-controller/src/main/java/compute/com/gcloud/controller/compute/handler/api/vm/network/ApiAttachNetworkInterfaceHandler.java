package com.gcloud.controller.compute.handler.api.vm.network;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.service.vm.netowork.IVmNetworkService;
import com.gcloud.controller.compute.workflow.model.network.AttachPortInitFlowCommandRes;
import com.gcloud.controller.compute.workflow.model.network.AttachPortWorkflowReq;
import com.gcloud.controller.compute.workflow.vm.network.AttachPortWorkflow;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.network.ApiAttachNetworkInterfaceMsg;
import com.gcloud.header.log.model.Task;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/15.
 */
@LongTask
@GcLog(isMultiLog = true, taskExpect = "挂载网卡")
@ApiHandler(module= Module.ECS, subModule = SubModule.NETWORKINTERFACE, action="AttachNetworkInterface")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.PORT, resourceIdField = "networkInterfaceId")
public class ApiAttachNetworkInterfaceHandler extends BaseWorkFlowHandler<ApiAttachNetworkInterfaceMsg, ApiReplyMessage> {

    @Autowired
    private IVmNetworkService vmNetworkService;

    @Override
    public Object preProcess(ApiAttachNetworkInterfaceMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public ApiReplyMessage process(ApiAttachNetworkInterfaceMsg msg) throws GCloudException {
        ApiReplyMessage replyMessage = new ApiReplyMessage();
        AttachPortInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), AttachPortInitFlowCommandRes.class);

        msg.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(msg.getInstanceId()).expect("挂载网卡成功").build());

        replyMessage.setTaskId(res.getTaskId());
        return replyMessage;

    }

    @Override
    public Class getWorkflowClass() {
        return AttachPortWorkflow.class;
    }

    @Override
    public Object initParams(ApiAttachNetworkInterfaceMsg msg) {
        AttachPortWorkflowReq req = new AttachPortWorkflowReq();
        req.setInstanceId(msg.getInstanceId());
        req.setNetworkInterfaceId(msg.getNetworkInterfaceId());
        req.setInTask(false);
        return req;
    }
}
