package com.gcloud.controller.compute.workflow.model.network;

/**
 * Created by yaowj on 2018/11/20.
 */
public class DetachPortWorkflowReq {

    private String instanceId;
    private String networkInterfaceId;
    private Boolean inTask = true;  //默认是true

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

    public Boolean getInTask() {
        return inTask;
    }

    public void setInTask(Boolean inTask) {
        this.inTask = inTask;
    }
}
