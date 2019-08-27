package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.ReplyMessage;

public class QueryInstanceVNCMsgReply extends ReplyMessage {
    private String port;

    private String hostIp;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }
}
