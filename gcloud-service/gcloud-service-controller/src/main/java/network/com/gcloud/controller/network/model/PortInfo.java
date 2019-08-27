package com.gcloud.controller.network.model;

public class PortInfo {

    private String id;
    private String networkId;
    private String subnetId;
    private String macAddress;
    private String ipAddress;
    private Integer provider;
    private String providerRefId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
    }

    public String getProviderRefId() {
        return providerRefId;
    }

    public void setProviderRefId(String providerRefId) {
        this.providerRefId = providerRefId;
    }
}
