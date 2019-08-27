package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NodeMessage;

/**
 * Created by yaowj on 2018/11/12.
 */
public class DestroyInstanceReplyMsg extends NodeMessage {

    private String instanceId;
    private String currentState;
    private Boolean handleResource;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public Boolean getHandleResource() {
        return handleResource;
    }

    public void setHandleResource(Boolean handleResource) {
        this.handleResource = handleResource;
    }
}
