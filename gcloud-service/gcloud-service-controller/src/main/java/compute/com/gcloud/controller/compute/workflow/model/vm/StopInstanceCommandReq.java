package com.gcloud.controller.compute.workflow.model.vm;

public class StopInstanceCommandReq {
    private String instanceId;
    private String beginningState;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getBeginningState() {
        return beginningState;
    }

    public void setBeginningState(String beginningState) {
        this.beginningState = beginningState;
    }
}
