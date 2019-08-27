package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.security.model.ClusterCreateNetcardInfo;

public class SecurityClusterCreateAttachInitFlowCommandReq {

    private String instanceId;
    private ClusterCreateNetcardInfo netcardInfo;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public ClusterCreateNetcardInfo getNetcardInfo() {
        return netcardInfo;
    }

    public void setNetcardInfo(ClusterCreateNetcardInfo netcardInfo) {
        this.netcardInfo = netcardInfo;
    }
}
