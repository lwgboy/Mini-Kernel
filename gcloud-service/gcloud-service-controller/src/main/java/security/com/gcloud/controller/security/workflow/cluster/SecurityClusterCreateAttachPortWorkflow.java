package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.SecurityClusterCreateAttachPortWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class SecurityClusterCreateAttachPortWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "SecurityClusterCreateAttachPortWorkflow";
    }

    @Override
    public Object preProcess() {
        return null;
    }

    @Override
    public void process() {

    }

    @Override
    protected Class<?> getReqParamClass() {
        return SecurityClusterCreateAttachPortWorkflowReq.class;
    }
}
