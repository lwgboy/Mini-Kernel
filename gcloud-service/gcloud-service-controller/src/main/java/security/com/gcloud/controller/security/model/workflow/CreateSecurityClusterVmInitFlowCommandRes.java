package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.security.model.ClusterCreateNetcardInfo;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.DiskInfo;

import java.util.List;

public class CreateSecurityClusterVmInitFlowCommandRes {

    private String imageId;
    private String instanceType;
    private String instanceName;
    private String hostName;
    private Integer systemDiskSize;
    private String systemDiskCategory;
    private List<DiskInfo> dataDisks;
    private Boolean zxAuth;
    private String zoneId;
    private String createHost;
    private String componentId;
    private CurrentUser currentUser;

    private List<ClusterCreateNetcardInfo> netcardInfos;

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

    public List<DiskInfo> getDataDisks() {
        return dataDisks;
    }

    public void setDataDisks(List<DiskInfo> dataDisks) {
        this.dataDisks = dataDisks;
    }

    public Boolean getZxAuth() {
        return zxAuth;
    }

    public void setZxAuth(Boolean zxAuth) {
        this.zxAuth = zxAuth;
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

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
