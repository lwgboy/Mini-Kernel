package com.gcloud.controller.compute.workflow.model.vm;

/**
 * Created by yaowj on 2018/11/26.
 */
public class DestroyInstanceIfExistFlowCommandReq {

    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
