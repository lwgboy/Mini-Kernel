package com.gcloud.header.compute.msg.node.vm.network;

import com.gcloud.header.NodeMessage;

/**
 * Created by yaowj on 2018/11/15.
 */
public class ForceDetachPortMsg extends NodeMessage {

    private String instanceId;
    private String portId;
    private String macAddress;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
