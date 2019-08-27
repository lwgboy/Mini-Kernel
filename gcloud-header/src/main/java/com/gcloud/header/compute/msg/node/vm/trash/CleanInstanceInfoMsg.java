package com.gcloud.header.compute.msg.node.vm.trash;

import com.gcloud.header.NodeMessage;

/**
 * Created by yaowj on 2018/11/28.
 */
public class CleanInstanceInfoMsg extends NodeMessage {

    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
