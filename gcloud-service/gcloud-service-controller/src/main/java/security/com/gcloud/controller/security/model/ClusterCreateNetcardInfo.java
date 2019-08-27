package com.gcloud.controller.security.model;

import com.gcloud.controller.network.enums.PortType;
import com.gcloud.controller.security.enums.SecurityNetcardConfigType;
import com.gcloud.controller.security.enums.SecurityNetworkType;
/**
 * Created by yaowj on 2018/7/30.
 */
public class ClusterCreateNetcardInfo {

    private String subnetId;
    private String securityGroupId;
    private PortType portType;
    private SecurityNetcardConfigType configType;
    private String clusterOvsId;
    private SecurityNetworkType securityNetworkType;

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public PortType getPortType() {
        return portType;
    }

    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    public SecurityNetcardConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(SecurityNetcardConfigType configType) {
        this.configType = configType;
    }

    public String getClusterOvsId() {
        return clusterOvsId;
    }

    public void setClusterOvsId(String clusterOvsId) {
        this.clusterOvsId = clusterOvsId;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public SecurityNetworkType getSecurityNetworkType() {
        return securityNetworkType;
    }

    public void setSecurityNetworkType(SecurityNetworkType securityNetworkType) {
        this.securityNetworkType = securityNetworkType;
    }
}
