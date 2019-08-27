package com.gcloud.controller.compute.workflow.model.network;

public class DetachAndDeleteNetcardWorkflowReq {

    private String instanceId;
    private String networkInterfaceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getNetworkInterfaceId() {
        return networkInterfaceId;
    }

    public void setNetworkInterfaceId(String networkInterfaceId) {
        this.networkInterfaceId = networkInterfaceId;
    }
}
