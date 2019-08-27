package com.gcloud.controller.compute.workflow.model.trash;

/**
 * Created by yaowj on 2018/12/4.
 */
public class ForceDetachAndDeleteDiskWorkflowReq {

    private DetachAndDeleteDiskInfo repeatParams;

    public DetachAndDeleteDiskInfo getRepeatParams() {
        return repeatParams;
    }

    public void setRepeatParams(DetachAndDeleteDiskInfo repeatParams) {
        this.repeatParams = repeatParams;
    }
}
