package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterDao;
import com.gcloud.controller.security.entity.SecurityCluster;
import com.gcloud.controller.security.enums.SecurityClusterState;
import com.gcloud.controller.security.model.workflow.DisableSecurityClusterHaDoneFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DisableSecurityClusterHaDoneFlowCommandRes;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
@Slf4j
public class DisableSecurityClusterHaDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterDao securityClusterDao;

    @Autowired
    private ISecurityClusterService securityClusterService;


    @Override
    protected Object process() throws Exception {

        DisableSecurityClusterHaDoneFlowCommandReq req = (DisableSecurityClusterHaDoneFlowCommandReq)getReqParams();

        List<String> updateField = new ArrayList<>();
        SecurityCluster cluster = new SecurityCluster();
        cluster.setId(req.getClusterId());
        updateField.add(cluster.updateState(SecurityClusterState.CREATED.value()));
        updateField.add(cluster.updateUpdateTime(new Date()));
        updateField.add(cluster.updateHa(false));

        securityClusterDao.update(cluster, updateField);

        securityClusterService.cleanClusterHaData(req.getClusterId());

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
        return DisableSecurityClusterHaDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DisableSecurityClusterHaDoneFlowCommandRes.class;
    }
}
