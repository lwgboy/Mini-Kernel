package com.gcloud.controller.compute.workflow.model.trash;

/**
 * Created by yaowj on 2018/12/4.
 */
public class ForceDetachAndDeleteNetcardWorkflowReq {

    private String instanceId;
    private DetachAndDeleteNetcardInfo repeatParams;

    public DetachAndDeleteNetcardInfo getRepeatParams() {
        return repeatParams;
    }

    public void setRepeatParams(DetachAndDeleteNetcardInfo repeatParams) {
        this.repeatParams = repeatParams;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
