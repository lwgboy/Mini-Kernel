package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterOvsBridgeDao;
import com.gcloud.controller.security.entity.SecurityClusterOvsBridge;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterOvsBridgeDoneFlowCommandReq;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterOvsBridgeDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterOvsBridgeDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterOvsBridgeDao securityClusterOvsBridgeDao;

    @Override
    protected Object process() throws Exception {

        CreateSecurityClusterOvsBridgeDoneFlowCommandReq request = (CreateSecurityClusterOvsBridgeDoneFlowCommandReq)getReqParams();

        List<String> updateField = new ArrayList<>();
        SecurityClusterOvsBridge ovsBridge = new SecurityClusterOvsBridge();
        ovsBridge.setId(request.getSecurityOvsBridgeId());

        updateField.add(ovsBridge.updateOvsBridgeId(request.getOvsBridgeId()));
        securityClusterOvsBridgeDao.update(ovsBridge, updateField);

        return null;
    }

    @Override
    protected Object rollback() throws Exception {

        CreateSecurityClusterOvsBridgeDoneFlowCommandReq request = (CreateSecurityClusterOvsBridgeDoneFlowCommandReq)getReqParams();
        securityClusterOvsBridgeDao.deleteById(request.getSecurityOvsBridgeId());

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return CreateSecurityClusterOvsBridgeDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreateSecurityClusterOvsBridgeDoneFlowCommandRes.class;
    }
}
