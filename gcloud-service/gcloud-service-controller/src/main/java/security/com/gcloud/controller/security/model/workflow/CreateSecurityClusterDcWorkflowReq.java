package com.gcloud.controller.security.model.workflow;

public class CreateSecurityClusterDcWorkflowReq {

    private CreateClusterComponentObjectInfo component;
    private String objectType;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public CreateClusterComponentObjectInfo getComponent() {
        return component;
    }

    public void setComponent(CreateClusterComponentObjectInfo component) {
        this.component = component;
    }
}
