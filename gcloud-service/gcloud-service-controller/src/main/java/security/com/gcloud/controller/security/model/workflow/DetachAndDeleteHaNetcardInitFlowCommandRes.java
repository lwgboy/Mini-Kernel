package com.gcloud.controller.security.model.workflow;

public class DetachAndDeleteHaNetcardInitFlowCommandRes  {

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
