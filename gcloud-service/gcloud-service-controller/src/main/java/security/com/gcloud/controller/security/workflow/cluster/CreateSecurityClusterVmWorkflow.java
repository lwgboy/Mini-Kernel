package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterVmWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterVmWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "CreateSecurityClusterVmWorkflow";
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
        CreateSecurityClusterVmWorkflowReq req = (CreateSecurityClusterVmWorkflowReq)getReqParams();
        return SecurityClusterComponentObjectType.VM.equals(req.getComponent().getObjectType());
    }

    @Override
    protected Class<?> getReqParamClass() {
        return CreateSecurityClusterVmWorkflowReq.class;
    }
}
