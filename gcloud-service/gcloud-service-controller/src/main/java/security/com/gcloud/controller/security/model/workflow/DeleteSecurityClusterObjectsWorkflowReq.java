package com.gcloud.controller.security.model.workflow;

import java.util.List;

public class DeleteSecurityClusterObjectsWorkflowReq {

    private List<SecurityClusterComponentInfo> components;

    public List<SecurityClusterComponentInfo> getComponents() {
        return components;
    }

    public void setComponents(List<SecurityClusterComponentInfo> components) {
        this.components = components;
    }
}
