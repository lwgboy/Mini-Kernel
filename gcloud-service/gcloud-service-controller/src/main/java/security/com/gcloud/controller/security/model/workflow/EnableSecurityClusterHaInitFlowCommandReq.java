package com.gcloud.controller.security.model.workflow;

import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.security.msg.model.CreateClusterInfoParams;
import com.gcloud.header.security.msg.model.CreateNetworkParams;

public class EnableSecurityClusterHaInitFlowCommandReq {

    private CurrentUser currentUser;
    private String id;
    private CreateClusterInfoParams createInfo;
    private CreateNetworkParams net;

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreateClusterInfoParams getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(CreateClusterInfoParams createInfo) {
        this.createInfo = createInfo;
    }

    public CreateNetworkParams getNet() {
        return net;
    }

    public void setNet(CreateNetworkParams net) {
        this.net = net;
    }
}
