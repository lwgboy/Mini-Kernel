package com.gcloud.service.common.compute.model;

public class DomainInterface {

    private String sourceBridge;
    private String targetDev;
    private String macAddress;

    public String getSourceBridge() {
        return sourceBridge;
    }

    public void setSourceBridge(String sourceBridge) {
        this.sourceBridge = sourceBridge;
    }

    public String getTargetDev() {
        return targetDev;
    }

    public void setTargetDev(String targetDev) {
        this.targetDev = targetDev;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
