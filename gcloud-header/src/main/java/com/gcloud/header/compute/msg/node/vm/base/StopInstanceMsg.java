package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NodeMessage;

/**
 * Created by yaowj on 2018/9/11.
 */
public class StopInstanceMsg extends NodeMessage {

    private String instanceId;
    private Boolean handleResource = true;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Boolean getHandleResource() {
        return handleResource;
    }

    public void setHandleResource(Boolean handleResource) {
        this.handleResource = handleResource;
    }
}