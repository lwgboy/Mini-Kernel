package com.gcloud.controller.security.model;

import com.gcloud.header.security.msg.model.CreateClusterInfoParams;
import com.gcloud.header.security.msg.model.CreateNetworkParams;

/**
 * Created by yaowj on 2018/7/6.
 */
public class EnableClusterHaParams {
    private String id;
    private CreateClusterInfoParams createInfo;
    private CreateNetworkParams net;

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
