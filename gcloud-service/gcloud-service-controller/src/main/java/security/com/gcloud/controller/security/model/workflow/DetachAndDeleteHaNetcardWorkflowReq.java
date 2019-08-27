package com.gcloud.controller.security.model.workflow;

import java.util.List;

public class DetachAndDeleteHaNetcardWorkflowReq {

    private List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos;

    public List<SecurityClusterComponentHaNetcardInfo> getHaNetcardInfos() {
        return haNetcardInfos;
    }

    public void setHaNetcardInfos(List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos) {
        this.haNetcardInfos = haNetcardInfos;
    }
}
