package com.gcloud.controller.compute.workflow.model.network;

import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;

/**
 * Created by yaowj on 2018/11/16.
 */
public class ForceCleanNetConfigFileFlowCommandReq {

    private String instanceId;
    private String userId;
    private VmNetworkDetail networkDetail;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
