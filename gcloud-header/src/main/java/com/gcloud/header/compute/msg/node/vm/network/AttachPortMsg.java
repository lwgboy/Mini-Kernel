package com.gcloud.header.compute.msg.node.vm.network;

import com.gcloud.header.NodeMessage;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;

/**
 * Created by yaowj on 2018/11/15.
 */
public class AttachPortMsg extends NodeMessage {

    private String instanceId;
    private VmNetworkDetail networkDetail;
    private Boolean noArpLimit;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public VmNetworkDetail getNetworkDetail() {
        return networkDetail;
    }

    public void setNetworkDetail(VmNetworkDetail networkDetail) {
        this.networkDetail = networkDetail;
    }

    public Boolean getNoArpLimit() {
        return noArpLimit;
    }

    public void setNoArpLimit(Boolean noArpLimit) {
        this.noArpLimit = noArpLimit;
    }
}
