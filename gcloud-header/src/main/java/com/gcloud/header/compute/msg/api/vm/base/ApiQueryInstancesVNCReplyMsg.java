package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiReplyMessage;

public class ApiQueryInstancesVNCReplyMsg extends ApiReplyMessage {

    private String vncUrl;

    public String getVncUrl() {
        return vncUrl;
    }

    public void setVncUrl(String vncUrl) {
        this.vncUrl = vncUrl;
    }
}
