package com.gcloud.controller.security.model.workflow;

public class CreateSecurityClusterVmInitFlowCommandReq {

    private CreateClusterComponentObjectInfo component;

    public CreateClusterComponentObjectInfo getComponent() {
        return component;
    }

    public void setComponent(CreateClusterComponentObjectInfo component) {
        this.component = component;
    }
}
