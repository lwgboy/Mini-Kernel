package com.gcloud.controller.compute.workflow.model.trash;

/**
 * Created by yaowj on 2018/12/4.
 */
public class ForceDetachAndDeleteDiskInitFlowCommandReq {

    private DetachAndDeleteDiskInfo disk;
    private String instanceId;

    public DetachAndDeleteDiskInfo getDisk() {
        return disk;
    }

    public void setDisk(DetachAndDeleteDiskInfo disk) {
        this.disk = disk;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
