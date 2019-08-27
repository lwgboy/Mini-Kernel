package com.gcloud.header.security.msg.model;

import com.gcloud.header.compute.msg.api.model.DiskInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaowj on 2018/7/11.
 */
public class CreateClusterVmParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private String instanceType;
    private String systemDiskCategory;
    private Integer systemDiskSize;
    private String imageId;
    private List<DiskInfo> dataDisks;

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

    public List<DiskInfo> getDataDisks() {
        return dataDisks;
    }

    public void setDataDisks(List<DiskInfo> dataDisks) {
        this.dataDisks = dataDisks;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }
}
