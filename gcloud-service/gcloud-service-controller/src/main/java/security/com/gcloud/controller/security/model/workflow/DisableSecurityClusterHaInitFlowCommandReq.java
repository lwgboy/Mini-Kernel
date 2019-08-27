package com.gcloud.controller.security.model.workflow;

import com.gcloud.header.api.model.CurrentUser;

public class DisableSecurityClusterHaInitFlowCommandReq {

    private CurrentUser currentUser;
    private String clusterId;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
