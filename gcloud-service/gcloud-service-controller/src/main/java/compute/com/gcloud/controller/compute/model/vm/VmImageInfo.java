package com.gcloud.controller.compute.model.vm;

public class VmImageInfo {
    private String imageStorageType;
    private String imagePoolId;
    private String imagePath;
    private String imageId;

    public String getImageStorageType() {
        return imageStorageType;
    }

    public void setImageStorageType(String imageStorageType) {
        this.imageStorageType = imageStorageType;
    }

    public String getImagePoolId() {
        return imagePoolId;
    }

    public void setImagePoolId(String imagePoolId) {
        this.imagePoolId = imagePoolId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
