package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterOvsBridgeDao;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterOvsBridgeDoneFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterOvsBridgeDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterOvsBridgeDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterOvsBridgeDao ovsBridgeDao;

    @Override
    protected Object process() throws Exception {

        DeleteSecurityClusterOvsBridgeDoneFlowCommandReq req = (DeleteSecurityClusterOvsBridgeDoneFlowCommandReq)getReqParams();
        ovsBridgeDao.deleteById(req.getClusterOvsBridgeId());

        return null;
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
        return DeleteSecurityClusterOvsBridgeDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteSecurityClusterOvsBridgeDoneFlowCommandRes.class;
    }
}
