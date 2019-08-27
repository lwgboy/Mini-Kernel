package com.gcloud.controller.storage.model;

/**
 * Created by yaowj on 2018/9/29.
 */
public class AttachDiskParams {

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
