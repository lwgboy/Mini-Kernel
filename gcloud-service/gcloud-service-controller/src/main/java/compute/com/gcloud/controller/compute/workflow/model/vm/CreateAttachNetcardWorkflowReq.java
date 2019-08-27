package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.api.model.CurrentUser;

public class CreateAttachNetcardWorkflowReq {
    private String instanceId;
    private String subnetId;
    private String ipAddress;
    private String securityGroupId;
    private String ovsBridgeId;
    private CurrentUser createUser;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getOvsBridgeId() {
        return ovsBridgeId;
    }

    public void setOvsBridgeId(String ovsBridgeId) {
        this.ovsBridgeId = ovsBridgeId;
    }

    public CurrentUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(CurrentUser createUser) {
        this.createUser = createUser;
    }
}
