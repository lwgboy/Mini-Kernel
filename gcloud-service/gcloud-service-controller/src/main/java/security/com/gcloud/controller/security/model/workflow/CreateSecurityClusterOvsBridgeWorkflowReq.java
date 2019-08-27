package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.security.model.ClusterCreateOvsBridgeInfo;

public class CreateSecurityClusterOvsBridgeWorkflowReq {

    private ClusterCreateOvsBridgeInfo bridgeInfo;

    public ClusterCreateOvsBridgeInfo getBridgeInfo() {
        return bridgeInfo;
    }

    public void setBridgeInfo(ClusterCreateOvsBridgeInfo bridgeInfo) {
        this.bridgeInfo = bridgeInfo;
    }
}
