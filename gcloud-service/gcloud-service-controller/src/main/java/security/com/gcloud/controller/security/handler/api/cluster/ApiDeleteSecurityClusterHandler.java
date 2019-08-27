package com.gcloud.controller.security.handler.api.cluster;

import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterInitFlowCommandRes;
import com.gcloud.controller.security.workflow.cluster.DeleteSecurityClusterWorkflow;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.log.model.Task;
import com.gcloud.header.security.msg.api.cluster.ApiDeleteSecurityClusterMsg;
import com.gcloud.header.security.msg.api.cluster.ApiDeleteSecurityClusterReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "DeleteSecurityCluster")
@LongTask
@GcLog(taskExpect = "删除安全集群")
public class ApiDeleteSecurityClusterHandler extends BaseWorkFlowHandler<ApiDeleteSecurityClusterMsg, ApiDeleteSecurityClusterReplyMsg> {

    @Override
    public Object preProcess(ApiDeleteSecurityClusterMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public ApiDeleteSecurityClusterReplyMsg process(ApiDeleteSecurityClusterMsg msg) throws GCloudException {

        ApiDeleteSecurityClusterReplyMsg replyMessage = new ApiDeleteSecurityClusterReplyMsg();
        DeleteSecurityClusterInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), DeleteSecurityClusterInitFlowCommandRes.class);

        msg.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(msg.getId()).expect("删除安全集群").build());

        replyMessage.setTaskId(res.getTaskId());
        replyMessage.setClusterId(msg.getId());

        return replyMessage;

    }

    @Override
    public Class getWorkflowClass() {
        return DeleteSecurityClusterWorkflow.class;
    }
}
