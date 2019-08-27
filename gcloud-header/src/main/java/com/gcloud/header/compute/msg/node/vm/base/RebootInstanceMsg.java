package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NodeMessage;

/**
 * Created by yaowj on 2018/9/11.
 */
public class RebootInstanceMsg extends NodeMessage {

    private String instanceId;
    private Boolean forceStop;


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Boolean getForceStop() {
        return forceStop;
    }

    public void setForceStop(Boolean forceStop) {
        this.forceStop = forceStop;
    }
}
