package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterDcWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterDcWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "CreateSecurityClusterDcWorkflow";
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
        CreateSecurityClusterDcWorkflowReq req = (CreateSecurityClusterDcWorkflowReq)getReqParams();
        return SecurityClusterComponentObjectType.DOCKER_CONTAINER.equals(req.getComponent().getObjectType());
    }

    @Override
    protected Class<?> getReqParamClass() {
        return CreateSecurityClusterDcWorkflowReq.class;
    }
}
