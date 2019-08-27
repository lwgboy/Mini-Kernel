package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.DiskInfo;

public class CreateAttachDataVolumeWorkflowReq {
    private String instanceId;
    private DiskInfo repeatParams;
    private CurrentUser createUser;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public DiskInfo getRepeatParams() {
        return repeatParams;
    }

    public void setRepeatParams(DiskInfo repeatParams) {
        this.repeatParams = repeatParams;
    }

    public CurrentUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(CurrentUser createUser) {
        this.createUser = createUser;
    }
}
