package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterDao;
import com.gcloud.controller.security.enums.SecurityClusterState;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterDoneFlowCommandReq;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterDao clusterDao;

    @Override
    protected Object process() throws Exception {

        CreateSecurityClusterDoneFlowCommandReq req = (CreateSecurityClusterDoneFlowCommandReq)getReqParams();
        clusterDao.changeState(req.getClusterId(), SecurityClusterState.CREATED.value());

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
        return CreateSecurityClusterDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreateSecurityClusterDoneFlowCommandRes.class;
    }
}
