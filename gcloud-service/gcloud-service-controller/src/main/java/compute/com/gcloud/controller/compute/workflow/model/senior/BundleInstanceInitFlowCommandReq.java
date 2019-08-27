package com.gcloud.controller.compute.workflow.model.senior;

import com.gcloud.header.api.model.CurrentUser;

/**
 * Created by yaowj on 2018/11/30.
 */
public class BundleInstanceInitFlowCommandReq {

    private String instanceId;
    private String imageName;
    private CurrentUser currentUser;
    private Boolean inTask = true;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Boolean getInTask() {
        return inTask;
    }

    public void setInTask(Boolean inTask) {
        this.inTask = inTask;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
