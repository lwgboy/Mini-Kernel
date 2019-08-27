package com.gcloud.controller.security.model.workflow;

public class DetachAndDeleteHaNetcardInitFlowCommandReq {

    private SecurityClusterComponentHaNetcardInfo haNetcardInfo;

    public SecurityClusterComponentHaNetcardInfo getHaNetcardInfo() {
        return haNetcardInfo;
    }

    public void setHaNetcardInfo(SecurityClusterComponentHaNetcardInfo haNetcardInfo) {
        this.haNetcardInfo = haNetcardInfo;
    }
}
