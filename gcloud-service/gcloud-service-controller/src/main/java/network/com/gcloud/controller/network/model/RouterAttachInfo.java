package com.gcloud.controller.network.model;

import com.gcloud.controller.network.entity.Port;

public class RouterAttachInfo {

    private String refPortId;
    private Port port;

    public String getRefPortId() {
        return refPortId;
    }

    public void setRefPortId(String refPortId) {
        this.refPortId = refPortId;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }
}
