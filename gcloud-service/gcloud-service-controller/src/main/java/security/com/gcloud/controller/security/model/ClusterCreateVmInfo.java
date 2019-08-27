package com.gcloud.controller.security.model;

import com.gcloud.controller.compute.entity.InstanceType;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.DiskInfo;

import java.util.List;

/**
 * Created by yaowj on 2018/8/1.
 */
public class ClusterCreateVmInfo {

    private String instanceName;
    private String systemDiskCategory;
    private Integer systemDiskSize;
    private String imageId;
    private List<DiskInfo> dataDisks;
    private InstanceType instanceType;
    private Boolean zxAuth;
    private String zoneId;
    private CurrentUser currentUser;

    private List<ClusterCreateNetcardInfo> netcardInfos;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public InstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
    }

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Boolean getZxAuth() {
        return zxAuth;
    }

    public void setZxAuth(Boolean zxAuth) {
        this.zxAuth = zxAuth;
    }

    public List<DiskInfo> getDataDisks() {
        return dataDisks;
    }

    public void setDataDisks(List<DiskInfo> dataDisks) {
        this.dataDisks = dataDisks;
    }

    public List<ClusterCreateNetcardInfo> getNetcardInfos() {
        return netcardInfos;
    }

    public void setNetcardInfos(List<ClusterCreateNetcardInfo> netcardInfos) {
        this.netcardInfos = netcardInfos;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
