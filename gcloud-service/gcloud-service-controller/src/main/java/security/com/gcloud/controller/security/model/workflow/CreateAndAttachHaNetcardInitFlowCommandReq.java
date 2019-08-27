package com.gcloud.controller.security.model.workflow;

import com.gcloud.header.api.model.CurrentUser;

public class CreateAndAttachHaNetcardInitFlowCommandReq {

    private CurrentUser currentUser;
    private SecurityClusterComponentHaNetcardInfo haNetcardInfo;

    public SecurityClusterComponentHaNetcardInfo getHaNetcardInfo() {
        return haNetcardInfo;
    }

    public void setHaNetcardInfo(SecurityClusterComponentHaNetcardInfo haNetcardInfo) {
        this.haNetcardInfo = haNetcardInfo;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
