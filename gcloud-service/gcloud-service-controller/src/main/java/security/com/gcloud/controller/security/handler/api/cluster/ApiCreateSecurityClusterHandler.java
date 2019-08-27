package com.gcloud.controller.security.handler.api.cluster;

import com.gcloud.controller.security.model.workflow.CreateSecurityClusterInitFlowCommandRes;
import com.gcloud.controller.security.workflow.cluster.CreateSecurityClusterWorkflow;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.workflow.core.handler.BaseWorkFlowHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.log.model.Task;
import com.gcloud.header.security.msg.api.cluster.ApiCreateSecurityClusterMsg;
import com.gcloud.header.security.msg.api.cluster.ApiCreateSecurityClusterReplyMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.SECURITYCLUSTER, action = "CreateSecurityCluster")
@LongTask
@GcLog(taskExpect = "创建安全集群")
public class ApiCreateSecurityClusterHandler extends BaseWorkFlowHandler<ApiCreateSecurityClusterMsg, ApiCreateSecurityClusterReplyMsg> {

    @Override
    public Object preProcess(ApiCreateSecurityClusterMsg msg) throws GCloudException {
        return null;
    }

    @Override
    public ApiCreateSecurityClusterReplyMsg process(ApiCreateSecurityClusterMsg msg) throws GCloudException {

        ApiCreateSecurityClusterReplyMsg replyMessage = new ApiCreateSecurityClusterReplyMsg();
        CreateSecurityClusterInitFlowCommandRes res = getFlowTaskFirstStepFirstRes(msg.getTaskId(), CreateSecurityClusterInitFlowCommandRes.class);

        msg.getTasks().add(Task.builder().taskId(res.getTaskId()).objectId(res.getClusterId()).expect("创建安全集群成功").build());

        replyMessage.setTaskId(res.getTaskId());
        replyMessage.setClusterId(res.getClusterId());
        return replyMessage;

    }

    @Override
    public Class getWorkflowClass() {
        return CreateSecurityClusterWorkflow.class;
    }
}
