package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterDcWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterDcWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "DeleteSecurityClusterDcWorkflow";
    }

    @Override
    public Object preProcess() {
        return null;
    }

    @Override
    public void process() {

    }

    @Override
    public boolean judgeExecute() {
        DeleteSecurityClusterDcWorkflowReq req = (DeleteSecurityClusterDcWorkflowReq)getReqParams();
        if(req != null && req.getComponent() != null && SecurityClusterComponentObjectType.DOCKER_CONTAINER.value().equals(req.getComponent().getObjectType())){
            return true;
        }
        return false;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteSecurityClusterDcWorkflowReq.class;
    }
}
