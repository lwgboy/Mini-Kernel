package com.gcloud.controller.compute.workflow.model.senior;

/**
 * Created by yaowj on 2018/11/30.
 */
public class BundleInstanceDoneFlowCommandReq {

    private String instanceId;
    private Boolean inTask = true;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Boolean getInTask() {
        return inTask;
    }

    public void setInTask(Boolean inTask) {
        this.inTask = inTask;
    }
}
