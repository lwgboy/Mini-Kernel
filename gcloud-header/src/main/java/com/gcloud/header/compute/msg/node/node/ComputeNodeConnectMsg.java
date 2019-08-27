package com.gcloud.header.compute.msg.node.node;

import com.gcloud.header.NodeMessage;
import com.gcloud.header.compute.msg.node.node.model.ComputeNodeInfo;

/**
 * Created by yaowj on 2018/10/18.
 */
public class ComputeNodeConnectMsg extends NodeMessage {

    private ComputeNodeInfo computeNodeInfo;
    private int nodeTimeout;

    public ComputeNodeInfo getComputeNodeInfo() {
        return computeNodeInfo;
    }

    public void setComputeNodeInfo(ComputeNodeInfo computeNodeInfo) {
        this.computeNodeInfo = computeNodeInfo;
    }

    public int getNodeTimeout() {
        return nodeTimeout;
    }

    public void setNodeTimeout(int nodeTimeout) {
        this.nodeTimeout = nodeTimeout;
    }
}
