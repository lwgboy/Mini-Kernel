package com.gcloud.controller.security.model.workflow;

import com.gcloud.header.api.model.CurrentUser;

public class DisableSecurityClusterHaWorkflowReq {

    private CurrentUser currentUser;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
