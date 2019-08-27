package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterComponentDao;
import com.gcloud.controller.security.entity.SecurityClusterComponent;
import com.gcloud.controller.security.enums.SecurityClusterComponentState;
import com.gcloud.controller.security.model.workflow.SecurityInstanceCreateDoneFlowCommandReq;
import com.gcloud.controller.security.model.workflow.SecurityInstanceCreateDoneFlowCommandRes;
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
public class SecurityInstanceCreateDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterComponentDao componentDao;

    @Override
    protected Object process() throws Exception {

        SecurityInstanceCreateDoneFlowCommandReq req = (SecurityInstanceCreateDoneFlowCommandReq)getReqParams();
        String instanceId = req.getInstanceInfo().get(0).getInstanceId();
        String componentId = req.getComponentId();

        List<String> updateField = new ArrayList<>();
        SecurityClusterComponent component = new SecurityClusterComponent();
        component.setId(componentId);
        updateField.add(component.updateUpdateTime(new Date()));
        updateField.add(component.updateState(SecurityClusterComponentState.CREATED.value()));
        updateField.add(component.updateObjectId(instanceId));
        componentDao.update(component, updateField);

        SecurityInstanceCreateDoneFlowCommandRes res = new SecurityInstanceCreateDoneFlowCommandRes();
        res.setInstanceId(instanceId);

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
        return SecurityInstanceCreateDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return SecurityInstanceCreateDoneFlowCommandRes.class;
    }
}
