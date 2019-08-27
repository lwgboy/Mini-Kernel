package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.network.service.IOvsBridgeService;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterOvsBridgeFlowCommandReq;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterOvsBridgeFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterOvsBridgeFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IOvsBridgeService ovsBridgeService;

    @Override
    protected Object process() throws Exception {

        CreateSecurityClusterOvsBridgeFlowCommandReq request = (CreateSecurityClusterOvsBridgeFlowCommandReq)getReqParams();
        String ovsBridgeId = ovsBridgeService.createOvsBridge(null, request.getBridgeInfo().getHostname(), null, this.getTaskId());

        CreateSecurityClusterOvsBridgeFlowCommandRes response = new CreateSecurityClusterOvsBridgeFlowCommandRes();
        response.setOvsBridgeId(ovsBridgeId);
        response.setSecurityOvsBridgeId(request.getBridgeInfo().getId());

        return response;
    }

    @Override
    protected Object rollback() throws Exception {

        CreateSecurityClusterOvsBridgeFlowCommandRes res = (CreateSecurityClusterOvsBridgeFlowCommandRes)getResParams();
        ovsBridgeService.deleteOvsBridge(res.getOvsBridgeId(), getTaskId());

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }


    @Override
    protected Class<?> getReqParamClass() {
        return CreateSecurityClusterOvsBridgeFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreateSecurityClusterOvsBridgeFlowCommandRes.class;
    }
}
