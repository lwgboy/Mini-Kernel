package com.gcloud.controller.security.handler.api.cluster;

import com.gcloud.controller.security.model.workflow.EnableSecurityClusterHaInitFlowCommandRes;
import com.gcloud.controller.security.workflow.cluster.EnableSecurityClusterHaWorkflow;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.log.model.Task;
import com.gcloud.header.security.msg.api.cluster.ApiEnableSecurityClusterHaMsg;
import com.gcloud.header.security.msg.api.cluster.ApiEnableSecurityClusterHaReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "EnableSecurityClusterHa")
@LongTask
@GcLog(taskExpect = "启用HA")
public class ApiEnableSecurityClusterHaHandler extends BaseWorkFlowHandler<ApiEnableSecurityClusterHaMsg, ApiEnableSecurityClusterHaReplyMsg> {

    @Override
    public Object preProcess(ApiEnableSecurityClusterHaMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public ApiEnableSecurityClusterHaReplyMsg process(ApiEnableSecurityClusterHaMsg msg) throws GCloudException {

        ApiEnableSecurityClusterHaReplyMsg replyMessage = new ApiEnableSecurityClusterHaReplyMsg();
        EnableSecurityClusterHaInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), EnableSecurityClusterHaInitFlowCommandRes.class);

        msg.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(res.getClusterId()).expect("安全集群启用HA成功").build());

        replyMessage.setTaskId(res.getTaskId());
        return replyMessage;

    }

    @Override
    public Class getWorkflowClass() {
        return EnableSecurityClusterHaWorkflow.class;
    }
}
