package com.gcloud.header.security.msg.model;

import java.io.Serializable;

/**
 * Created by yaowj on 2018/7/30.
 */
public class CreateHaInfoParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private CreateNetworkParams net;
    private CreateClusterInfoParams createInfo;

    public CreateNetworkParams getNet() {
        return net;
    }

    public void setNet(CreateNetworkParams net) {
        this.net = net;
    }

    public CreateClusterInfoParams getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(CreateClusterInfoParams createInfo) {
        this.createInfo = createInfo;
    }
}
