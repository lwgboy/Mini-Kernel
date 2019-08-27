package com.gcloud.controller.security.model.workflow;

public class CreateSecurityClusterOvsBridgeFlowCommandRes {

    private String securityOvsBridgeId;
    private String ovsBridgeId;

    public String getOvsBridgeId() {
        return ovsBridgeId;
    }

    public void setOvsBridgeId(String ovsBridgeId) {
        this.ovsBridgeId = ovsBridgeId;
    }

    public String getSecurityOvsBridgeId() {
        return securityOvsBridgeId;
    }

    public void setSecurityOvsBridgeId(String securityOvsBridgeId) {
        this.securityOvsBridgeId = securityOvsBridgeId;
    }
}
