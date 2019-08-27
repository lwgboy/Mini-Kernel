package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiMessage;

public class ApiDeleteSecurityClusterMsg extends ApiMessage {

    @Override
    public Class replyClazz() {
        return ApiDeleteSecurityClusterReplyMsg.class;
    }

    private static final long serialVersionUID = 1L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
