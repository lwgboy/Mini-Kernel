package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterObjectInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterObjectInitFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterObjectInitFlowCommand extends BaseWorkFlowCommand {

    @Override
    protected Object process() throws Exception {

        DeleteSecurityClusterObjectInitFlowCommandReq req = (DeleteSecurityClusterObjectInitFlowCommandReq)getReqParams();

        DeleteSecurityClusterObjectInitFlowCommandRes res = new DeleteSecurityClusterObjectInitFlowCommandRes();
        res.setComponent(req.getComponent());

        return res;
    }

    @Override
    protected Object rollback() throws Exception {
        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteSecurityClusterObjectInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteSecurityClusterObjectInitFlowCommandRes.class;
    }
}
