package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.compute.workflow.model.vm.CreateInstanceFlowDoneCommandRes;

import java.util.List;

public class SecurityInstanceCreateDoneFlowCommandReq {

    private List<CreateInstanceFlowDoneCommandRes> instanceInfo;
    private String componentId;

    public List<CreateInstanceFlowDoneCommandRes> getInstanceInfo() {
        return instanceInfo;
    }

    public void setInstanceInfo(List<CreateInstanceFlowDoneCommandRes> instanceInfo) {
        this.instanceInfo = instanceInfo;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }
}
