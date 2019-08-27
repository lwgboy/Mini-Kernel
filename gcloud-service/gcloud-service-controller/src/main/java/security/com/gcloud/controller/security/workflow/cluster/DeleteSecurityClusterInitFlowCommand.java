package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterComponentDao;
import com.gcloud.controller.security.dao.SecurityClusterOvsBridgeDao;
import com.gcloud.controller.security.entity.SecurityClusterComponent;
import com.gcloud.controller.security.entity.SecurityClusterOvsBridge;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DeleteSecurityClusterInitFlowCommandRes;
import com.gcloud.controller.security.model.workflow.SecurityClusterComponentInfo;
import com.gcloud.controller.security.model.workflow.SecurityClusterOvsBridgeInfo;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
@Slf4j
public class DeleteSecurityClusterInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterComponentDao clusterComponentDao;

    @Autowired
    private SecurityClusterOvsBridgeDao clusterOvsBridgeDao;

    @Autowired
    private ISecurityClusterService securityClusterService;

    @Override
    protected Object process() throws Exception {

        DeleteSecurityClusterInitFlowCommandReq req = (DeleteSecurityClusterInitFlowCommandReq)getReqParams();

        securityClusterService.delete(req.getClusterId());

        List<SecurityClusterComponentInfo> components = clusterComponentDao.findByProperty(SecurityClusterComponent.CLUSTER_ID, req.getClusterId(), SecurityClusterComponentInfo.class);
        List<SecurityClusterOvsBridgeInfo> ovsBridges = clusterOvsBridgeDao.findByProperty(SecurityClusterOvsBridge.CLUSTER_ID, req.getClusterId(), SecurityClusterOvsBridgeInfo.class);

        DeleteSecurityClusterInitFlowCommandRes res = new DeleteSecurityClusterInitFlowCommandRes();
        res.setComponents(components);
        res.setOvsBridges(ovsBridges);

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
        return DeleteSecurityClusterInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteSecurityClusterInitFlowCommandRes.class;
    }
}
