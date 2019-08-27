package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterOvsBridgesWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterOvsBridgesWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "DeleteSecurityClusterOvsBridgesWorkflow";
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
        DeleteSecurityClusterOvsBridgesWorkflowReq req = (DeleteSecurityClusterOvsBridgesWorkflowReq)getReqParams();
        if(req != null && req.getOvsBridges() != null && req.getOvsBridges().size() > 0){
            return true;
        }
        return false;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteSecurityClusterOvsBridgesWorkflowReq.class;
    }
}
