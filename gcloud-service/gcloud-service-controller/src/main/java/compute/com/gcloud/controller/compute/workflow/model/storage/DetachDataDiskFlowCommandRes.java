package com.gcloud.controller.compute.workflow.model.storage;

public class DetachDataDiskFlowCommandRes {

    private String volumeId;
    private String instanceUuid;
    private String mountPoint;
    private String attachedHost;

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getInstanceUuid() {
        return instanceUuid;
    }

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid;
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public String getAttachedHost() {
        return attachedHost;
    }

    public void setAttachedHost(String attachedHost) {
        this.attachedHost = attachedHost;
    }
}
