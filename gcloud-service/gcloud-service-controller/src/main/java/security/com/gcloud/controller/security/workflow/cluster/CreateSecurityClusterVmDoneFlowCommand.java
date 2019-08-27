package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.CreateSecurityClusterVmDoneFlowCommandReq;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterVmDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterVmDoneFlowCommand extends BaseWorkFlowCommand {

    @Override
    protected Object process() throws Exception {
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
        return CreateSecurityClusterVmDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreateSecurityClusterVmDoneFlowCommandRes.class;
    }
}
