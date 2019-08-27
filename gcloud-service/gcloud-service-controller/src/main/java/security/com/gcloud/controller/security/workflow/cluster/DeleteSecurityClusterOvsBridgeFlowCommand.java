package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.network.dao.OvsBridgeDao;
import com.gcloud.controller.network.service.IOvsBridgeService;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterOvsBridgeFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterOvsBridgeFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterOvsBridgeFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IOvsBridgeService ovsBridgeService;

    @Autowired
    private OvsBridgeDao ovsBridgeDao;

    @Override
    protected Object process() throws Exception {

        DeleteSecurityClusterOvsBridgeFlowCommandReq req = (DeleteSecurityClusterOvsBridgeFlowCommandReq)getReqParams();
        ovsBridgeService.deleteOvsBridge(req.getOvsBridgeInfo().getOvsBridgeId(), getTaskId());

        DeleteSecurityClusterOvsBridgeFlowCommandRes res = new DeleteSecurityClusterOvsBridgeFlowCommandRes();
        res.setClusterOvsBridgeId(req.getOvsBridgeInfo().getOvsBridgeId());

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
        return DeleteSecurityClusterOvsBridgeFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteSecurityClusterOvsBridgeFlowCommandRes.class;
    }
}
