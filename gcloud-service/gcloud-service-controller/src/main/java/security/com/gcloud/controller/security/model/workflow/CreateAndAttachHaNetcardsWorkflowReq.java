package com.gcloud.controller.security.model.workflow;

import com.gcloud.header.api.model.CurrentUser;

import java.util.List;

public class CreateAndAttachHaNetcardsWorkflowReq {

    private CurrentUser currentUser;
    private List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos;

    public List<SecurityClusterComponentHaNetcardInfo> getHaNetcardInfos() {
        return haNetcardInfos;
    }

    public void setHaNetcardInfos(List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos) {
        this.haNetcardInfos = haNetcardInfos;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
