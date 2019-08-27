package com.gcloud.controller.compute.workflow.model.vm;

/**
 * Created by yaowj on 2018/10/9.
 */
public class VolumeAttachWorkflowReq {

    private String instanceId;
    private String diskId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}
