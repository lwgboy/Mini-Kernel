package com.gcloud.service.common.compute.model;

/**
 * Created by yaowj on 2019/1/11.
 */
public class DomainInfo {

    private String instanceId;
    private String state;
    private Boolean persistent;
    private Boolean autostart;
    private String gcState;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }

    public Boolean getAutostart() {
        return autostart;
    }

    public void setAutostart(Boolean autostart) {
        this.autostart = autostart;
    }

    public String getGcState() {
        return gcState;
    }

    public void setGcState(String gcState) {
        this.gcState = gcState;
    }
}
