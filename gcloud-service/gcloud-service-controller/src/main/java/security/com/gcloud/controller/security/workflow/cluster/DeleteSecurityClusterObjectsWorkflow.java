package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterObjectsWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterObjectsWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "DeleteSecurityClusterObjectsWorkflow";
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
        DeleteSecurityClusterObjectsWorkflowReq req = (DeleteSecurityClusterObjectsWorkflowReq)getReqParams();
        if(req != null && req.getComponents() != null && req.getComponents().size() > 0){
            return true;
        }
        return false;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteSecurityClusterObjectsWorkflowReq.class;
    }
}
