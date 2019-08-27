package com.gcloud.controller.compute.workflow.model.trash;

import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;

/**
 * Created by yaowj on 2018/12/4.
 */
public class ForceDetachAndDeleteNetcardInitFlowCommandRes {

    private String instanceId;
    private String netcardId;
    private VmNetworkDetail networkDetail;
    private Boolean delete = true;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getNetcardId() {
        return netcardId;
    }

    public void setNetcardId(String netcardId) {
        this.netcardId = netcardId;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public VmNetworkDetail getNetworkDetail() {
        return networkDetail;
    }

    public void setNetworkDetail(VmNetworkDetail networkDetail) {
        this.networkDetail = networkDetail;
    }
}
