package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.DiskInfo;

import java.util.List;

public class CreateInstanceFlowInitCommandReq {

    private String systemDiskName;
    private String imageId;
    private String instanceType;
    private String instanceName;
    private String hostName;
    private String password;
    private String subnetId;
    private String ipAddress;
    private List<DiskInfo> dataDisk;
    private Integer systemDiskSize;
    private String systemDiskCategory;
    private String zoneId;
    private Integer cpu;
    private Integer memory;
    private String securityGroupId;
    //private String curUserId;
    private CurrentUser currentUser;
    private Boolean inTask = true;
    private Boolean zxAuth;
    private String createHost;

    public String getSystemDiskName() {
        return systemDiskName;
    }

    public void setSystemDiskName(String systemDiskName) {
        this.systemDiskName = systemDiskName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<DiskInfo> getDataDisk() {
        return dataDisk;
    }

    public void setDataDisk(List<DiskInfo> dataDisk) {
        this.dataDisk = dataDisk;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public Boolean getInTask() {
        return inTask;
    }

    public void setInTask(Boolean inTask) {
        this.inTask = inTask;
    }

    public Boolean getZxAuth() {
        return zxAuth;
    }

    public void setZxAuth(Boolean zxAuth) {
        this.zxAuth = zxAuth;
    }

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }
}
