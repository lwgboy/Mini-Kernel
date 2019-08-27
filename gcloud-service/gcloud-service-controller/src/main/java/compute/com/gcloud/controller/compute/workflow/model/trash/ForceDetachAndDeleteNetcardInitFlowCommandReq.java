package com.gcloud.controller.compute.workflow.model.trash;

/**
 * Created by yaowj on 2018/12/4.
 */
public class ForceDetachAndDeleteNetcardInitFlowCommandReq {

    private DetachAndDeleteNetcardInfo netcard;
    private String instanceId;

    public DetachAndDeleteNetcardInfo getNetcard() {
        return netcard;
    }

    public void setNetcard(DetachAndDeleteNetcardInfo netcard) {
        this.netcard = netcard;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
