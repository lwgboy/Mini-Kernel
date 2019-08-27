package com.gcloud.header.compute.model;

import java.util.Date;

/**
 * Created by yaowj on 2018/10/24.
 */
public class VmNetPortsInfo {

    private String instanceId;
    private String portId;
    private String ip;
    private String sufId;
    private String brName;
    private String aftName;
    private String preName;
    private Date createTime;
    private String subnetId;
    private String customOvsBr;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSufId() {
        return sufId;
    }

    public void setSufId(String sufId) {
        this.sufId = sufId;
    }

    public String getBrName() {
        return brName;
    }

    public void setBrName(String brName) {
        this.brName = brName;
    }

    public String getAftName() {
        return aftName;
    }

    public void setAftName(String aftName) {
        this.aftName = aftName;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getCustomOvsBr() {
        return customOvsBr;
    }

    public void setCustomOvsBr(String customOvsBr) {
        this.customOvsBr = customOvsBr;
    }
}
