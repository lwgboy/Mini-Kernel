package com.gcloud.controller.security.model.workflow;

import java.util.List;

public class DeleteSecurityClusterOvsBridgesWorkflowReq {

    private List<SecurityClusterOvsBridgeInfo> ovsBridges;

    public List<SecurityClusterOvsBridgeInfo> getOvsBridges() {
        return ovsBridges;
    }

    public void setOvsBridges(List<SecurityClusterOvsBridgeInfo> ovsBridges) {
        this.ovsBridges = ovsBridges;
    }
}
