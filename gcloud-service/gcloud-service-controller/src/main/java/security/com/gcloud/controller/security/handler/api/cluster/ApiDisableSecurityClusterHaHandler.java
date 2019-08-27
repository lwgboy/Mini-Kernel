package com.gcloud.controller.security.handler.api.cluster;

import com.gcloud.controller.security.model.workflow.DisableSecurityClusterHaInitFlowCommandRes;
import com.gcloud.controller.security.workflow.cluster.DisableSecurityClusterHaWorkflow;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.log.model.Task;
import com.gcloud.header.security.msg.api.cluster.ApiDisableSecurityClusterHaMsg;
import com.gcloud.header.security.msg.api.cluster.ApiDisableSecurityClusterHaReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "DisableSecurityClusterHa")
@LongTask
@GcLog(taskExpect = "禁用HA")
public class ApiDisableSecurityClusterHaHandler extends BaseWorkFlowHandler<ApiDisableSecurityClusterHaMsg, ApiDisableSecurityClusterHaReplyMsg> {

    @Override
    public Object preProcess(ApiDisableSecurityClusterHaMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public ApiDisableSecurityClusterHaReplyMsg process(ApiDisableSecurityClusterHaMsg msg) throws GCloudException {

        ApiDisableSecurityClusterHaReplyMsg replyMessage = new ApiDisableSecurityClusterHaReplyMsg();
        DisableSecurityClusterHaInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), DisableSecurityClusterHaInitFlowCommandRes.class);

        msg.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(res.getClusterId()).expect("安全集群禁用HA成功").build());

        replyMessage.setTaskId(res.getTaskId());
        return replyMessage;

    }

    @Override
    public Class getWorkflowClass() {
        return DisableSecurityClusterHaWorkflow.class;
    }
}
