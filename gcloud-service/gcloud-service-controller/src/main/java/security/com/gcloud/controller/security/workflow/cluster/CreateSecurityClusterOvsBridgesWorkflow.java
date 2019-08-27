package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.CreateSecurityClusterOvsBridgeWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterOvsBridgesWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "CreateSecurityClusterOvsBridgesWorkflow";
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
        return CreateSecurityClusterOvsBridgeWorkflowReq.class;
    }

}
