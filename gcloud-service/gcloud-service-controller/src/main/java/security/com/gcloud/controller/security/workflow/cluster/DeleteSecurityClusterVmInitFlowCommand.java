package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterVmInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterVmInitFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterVmInitFlowCommand extends BaseWorkFlowCommand {

    @Override
    protected Object process() throws Exception {

        DeleteSecurityClusterVmInitFlowCommandReq req = (DeleteSecurityClusterVmInitFlowCommandReq)getReqParams();

        DeleteSecurityClusterVmInitFlowCommandRes res = new DeleteSecurityClusterVmInitFlowCommandRes();
        res.setInstanceId(req.getComponent().getObjectId());
        res.setComponentId(req.getComponent().getId());
        res.setDeleteInTask(false);
        res.setDeleteNotExist(true);

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
        return DeleteSecurityClusterVmInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteSecurityClusterVmInitFlowCommandRes.class;
    }
}
