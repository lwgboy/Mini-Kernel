package com.gcloud.controller.network.model;

/**
 * Created by yaowj on 2018/10/25.
 */
public class CreatePortParams {

    private String name;
    private String subnetId;
    private String subnetRefId;
    private String securityGroupId;
    private String securityGroupRefId;
    private String ipAddress;
    private String description;
    private Integer provider;
    private String ovsBridgeId;
    private String networkId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
    }

    public String getSubnetRefId() {
        return subnetRefId;
    }

    public void setSubnetRefId(String subnetRefId) {
        this.subnetRefId = subnetRefId;
    }

    public String getSecurityGroupRefId() {
        return securityGroupRefId;
    }

    public void setSecurityGroupRefId(String securityGroupRefId) {
        this.securityGroupRefId = securityGroupRefId;
    }

    public String getOvsBridgeId() {
        return ovsBridgeId;
    }

    public void setOvsBridgeId(String ovsBridgeId) {
        this.ovsBridgeId = ovsBridgeId;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }
}
