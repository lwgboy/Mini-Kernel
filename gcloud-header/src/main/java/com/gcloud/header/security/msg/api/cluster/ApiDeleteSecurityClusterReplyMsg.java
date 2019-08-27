package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiReplyMessage;

public class ApiDeleteSecurityClusterReplyMsg extends ApiReplyMessage {

    private static final long serialVersionUID = 1L;

    private String clusterId;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
}
