package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.EnableClusterHaParams;
import com.gcloud.controller.security.model.EnableClusterHaResponse;
import com.gcloud.controller.security.model.workflow.EnableSecurityClusterHaInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.EnableSecurityClusterHaInitFlowCommandRes;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class EnableSecurityClusterHaInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private ISecurityClusterService clusterService;

    @Override
    protected Object process() throws Exception {

        EnableSecurityClusterHaInitFlowCommandReq req = (EnableSecurityClusterHaInitFlowCommandReq)getReqParams();
        EnableSecurityClusterHaInitFlowCommandRes res = new EnableSecurityClusterHaInitFlowCommandRes();
        EnableClusterHaParams params = BeanUtil.copyBean(req, EnableClusterHaParams.class);

        EnableClusterHaResponse enableHaInfo = clusterService.enableHa(params, req.getCurrentUser());

        res.setBridgeInfos(enableHaInfo.getBridgeInfos());
        res.setComponents(enableHaInfo.getComponents());
        res.setHaNetcardInfos(enableHaInfo.getHaNetcardInfos());
        res.setClusterId(enableHaInfo.getId());

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
        return EnableSecurityClusterHaInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return EnableSecurityClusterHaInitFlowCommandRes.class;
    }
}
