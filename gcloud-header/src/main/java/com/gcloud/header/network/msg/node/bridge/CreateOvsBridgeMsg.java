package com.gcloud.header.network.msg.node.bridge;

import com.gcloud.header.NodeMessage;

public class CreateOvsBridgeMsg extends NodeMessage {
    private static final long serialVersionUID = 1L;

    private String id;
    private String bridge;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }
}
