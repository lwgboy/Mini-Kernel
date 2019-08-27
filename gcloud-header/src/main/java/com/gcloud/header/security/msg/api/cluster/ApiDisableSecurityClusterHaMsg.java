package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiMessage;

public class ApiDisableSecurityClusterHaMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiDisableSecurityClusterHaReplyMsg.class;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
