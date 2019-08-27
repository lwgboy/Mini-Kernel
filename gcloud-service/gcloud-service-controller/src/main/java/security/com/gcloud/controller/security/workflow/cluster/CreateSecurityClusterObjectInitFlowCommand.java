package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterComponentDao;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterObjectInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterObjectInitFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterObjectInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterComponentDao securityClusterComponentDao;

    @Override
    protected Object process() throws Exception {

        CreateSecurityClusterObjectInitFlowCommandReq req = (CreateSecurityClusterObjectInitFlowCommandReq)getReqParams();

        CreateSecurityClusterObjectInitFlowCommandRes res = new CreateSecurityClusterObjectInitFlowCommandRes();
        res.setComponent(req.getComponent());
        return res;
    }

    @Override
    protected Object rollback() throws Exception {
        CreateSecurityClusterObjectInitFlowCommandReq req = (CreateSecurityClusterObjectInitFlowCommandReq)getReqParams();
        securityClusterComponentDao.deleteById(req.getComponent().getComponentId());
        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return CreateSecurityClusterObjectInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreateSecurityClusterObjectInitFlowCommandRes.class;
    }
}
