package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.api.model.CurrentUser;

public class CheckCreateVmNodeEnvFlowCommandReq {
    private CurrentUser createUser;
    private String instanceId;
    private String createHost;

    public CurrentUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(CurrentUser createUser) {
        this.createUser = createUser;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }
}
