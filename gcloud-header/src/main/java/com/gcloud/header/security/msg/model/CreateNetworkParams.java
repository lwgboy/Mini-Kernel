package com.gcloud.header.security.msg.model;

import java.io.Serializable;

/**
 * Created by yaowj on 2018/7/30.
 */
public class CreateNetworkParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private String securityGroupId;
    private String subnetId;
    private String networkId;

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }
}
