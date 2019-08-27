package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterComponentDao;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterVmDoneFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterVmDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterVmDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterComponentDao componentDao;

    @Override
    protected Object process() throws Exception {
        DeleteSecurityClusterVmDoneFlowCommandReq req = (DeleteSecurityClusterVmDoneFlowCommandReq)getReqParams();
        componentDao.deleteById(req.getComponentId());
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
        return DeleteSecurityClusterVmDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteSecurityClusterVmDoneFlowCommandRes.class;
    }
}
