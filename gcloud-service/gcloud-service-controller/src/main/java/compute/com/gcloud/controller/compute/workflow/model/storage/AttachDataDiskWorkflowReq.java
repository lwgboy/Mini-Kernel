package com.gcloud.controller.compute.workflow.model.storage;

public class AttachDataDiskWorkflowReq {
    private String instanceId;
    private String volumeId;
    private Boolean inTask = true;  //默认是true

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public Boolean getInTask() {
        return inTask;
    }

    public void setInTask(Boolean inTask) {
        this.inTask = inTask;
    }
}
