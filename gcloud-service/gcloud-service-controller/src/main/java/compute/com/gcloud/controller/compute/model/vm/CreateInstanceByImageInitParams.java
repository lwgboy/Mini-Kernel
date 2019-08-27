package com.gcloud.controller.compute.model.vm;

import com.gcloud.header.compute.msg.api.model.DiskInfo;

import java.util.List;

/**
 * Created by yaowj on 2019/1/8.
 */
public class CreateInstanceByImageInitParams {

    private String imageId;
    private String systemDiskCategory;
    private Boolean handleResource = true; //默认处理资源
    private String createHost;
    private String instanceName;
    private Integer systemDiskSize;
    private String curUserId;
    private String instanceType;

    private List<DiskInfo> dataDisk;
    private String subnetId;
    private String ipAddress;
    
    private String zoneId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory;
    }


    public Boolean getHandleResource() {
        return handleResource;
    }

    public void setHandleResource(Boolean handleResource) {
        this.handleResource = handleResource;
    }

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public String getCurUserId() {
        return curUserId;
    }

    public void setCurUserId(String curUserId) {
        this.curUserId = curUserId;
    }

    public List<DiskInfo> getDataDisk() {
        return dataDisk;
    }

    public void setDataDisk(List<DiskInfo> dataDisk) {
        this.dataDisk = dataDisk;
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

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }
}
