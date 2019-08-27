package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterDoneFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterDoneFlowCommandRes;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private ISecurityClusterService clusterService;

    @Override
    protected Object process() throws Exception {

        DeleteSecurityClusterDoneFlowCommandReq req = (DeleteSecurityClusterDoneFlowCommandReq)getReqParams();
        clusterService.cleanClusterData(req.getClusterId());
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
        return DeleteSecurityClusterDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteSecurityClusterDoneFlowCommandRes.class;
    }
}
