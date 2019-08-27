package com.gcloud.controller.compute.workflow.model.trash;

/**
 * Created by yaowj on 2018/12/4.
 */
public class ForceDetachNetcardDoneFlowCommandReq {

    private String netcardId;
    private String instanceId;

    public String getNetcardId() {
        return netcardId;
    }

    public void setNetcardId(String netcardId) {
        this.netcardId = netcardId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
