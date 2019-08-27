package com.gcloud.controller.compute.model.vm;

/**
 * Created by yaowj on 2019/1/4.
 */
public class CreateInstanceByImageResponse {

    private String storageType;
    private String id;
    private VmImageInfo imageInfo;

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
}
