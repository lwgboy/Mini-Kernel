package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.workflow.model.network.DetachAndDeleteNetcardWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DetachAndDeleteNetcardWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "DetachAndDeleteNetcardWorkflow";
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
        return DetachAndDeleteNetcardWorkflowReq.class;
    }
}
