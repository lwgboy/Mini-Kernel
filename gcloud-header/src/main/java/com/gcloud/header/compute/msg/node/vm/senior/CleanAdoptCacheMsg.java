package com.gcloud.header.compute.msg.node.vm.senior;

import com.gcloud.header.NodeMessage;

/**
 * Created by yaowj on 2018/9/11.
 */
public class CleanAdoptCacheMsg extends NodeMessage {

    private String hostname;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
