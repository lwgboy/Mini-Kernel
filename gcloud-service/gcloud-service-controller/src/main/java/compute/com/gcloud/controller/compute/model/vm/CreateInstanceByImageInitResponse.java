package com.gcloud.controller.compute.model.vm;

import com.gcloud.header.api.model.CurrentUser;

/**
 * Created by yaowj on 2019/1/8.
 */
public class CreateInstanceByImageInitResponse {

    private String id;
    private String createHost;
    private String storageType;
    private VmImageInfo imageInfo;
    private Integer cpu;
    private Integer memory;
    private Integer systemDiskSize;
    private CurrentUser createUser;

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VmImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(VmImageInfo imageInfo) {
        this.imageInfo = imageInfo;
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

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public CurrentUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(CurrentUser createUser) {
        this.createUser = createUser;
    }
}
