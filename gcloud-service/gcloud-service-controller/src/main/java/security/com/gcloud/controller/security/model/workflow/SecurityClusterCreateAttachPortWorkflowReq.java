package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.security.model.ClusterCreateNetcardInfo;

public class SecurityClusterCreateAttachPortWorkflowReq {

    private String instanceId;
    private ClusterCreateNetcardInfo repeatParams;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public ClusterCreateNetcardInfo getRepeatParams() {
        return repeatParams;
    }

    public void setRepeatParams(ClusterCreateNetcardInfo repeatParams) {
        this.repeatParams = repeatParams;
    }
}
