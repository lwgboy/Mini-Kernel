package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.security.model.ClusterCreateNetcardInfo;

public class SecurityClusterComponentHaNetcardInfo {

    private String componentId;
    private String objectId;
    private String objectType;
    private String netcardId;
    private ClusterCreateNetcardInfo netcardInfo;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public ClusterCreateNetcardInfo getNetcardInfo() {
        return netcardInfo;
    }

    public void setNetcardInfo(ClusterCreateNetcardInfo netcardInfo) {
        this.netcardInfo = netcardInfo;
    }

    public String getNetcardId() {
        return netcardId;
    }

    public void setNetcardId(String netcardId) {
        this.netcardId = netcardId;
    }
}
