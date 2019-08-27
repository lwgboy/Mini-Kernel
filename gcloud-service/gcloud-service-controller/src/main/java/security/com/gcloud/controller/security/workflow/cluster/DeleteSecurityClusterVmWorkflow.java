package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterVmWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterVmWorkflow extends BaseWorkFlows{

    @Override
    public String getFlowTypeCode() {
        return "DeleteSecurityClusterVmWorkflow";
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
        DeleteSecurityClusterVmWorkflowReq req = (DeleteSecurityClusterVmWorkflowReq)getReqParams();
        if(req != null && req.getComponent() != null && SecurityClusterComponentObjectType.VM.value().equals(req.getComponent().getObjectType())){
            return true;
        }
        return false;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteSecurityClusterVmWorkflowReq.class;
    }
}
