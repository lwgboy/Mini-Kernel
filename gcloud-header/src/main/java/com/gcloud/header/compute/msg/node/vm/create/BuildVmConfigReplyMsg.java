package com.gcloud.header.compute.msg.node.vm.create;

import com.gcloud.header.NodeMessage;

public class BuildVmConfigReplyMsg  extends NodeMessage{
	private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
