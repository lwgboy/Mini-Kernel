package com.gcloud.header.compute.msg.node.vm.senior;

import com.gcloud.header.NodeMessage;

/**
 * Created by yaowj on 2018/11/30.
 */
public class DeleteBundleMsg extends NodeMessage {

    private String instanceId;
    private String nodeIp;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

}
