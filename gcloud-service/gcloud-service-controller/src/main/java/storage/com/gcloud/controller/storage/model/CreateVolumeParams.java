package com.gcloud.controller.storage.model;

import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.header.storage.enums.DiskType;

/**
 * Created by yaowj on 2018/9/21.
 */
public class CreateVolumeParams {

    private String diskName;
    private String description;
    private Integer size;
    private String snapshotId;
    private String diskCategory;
    private DiskType diskType;
    private boolean bootable;
    private String imageRef;
    private String zoneId;

    private String imageProviderRefId;
    private Integer imageProvider;

    private StoragePool pool;
    
    private String taskId;


    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getDiskCategory() {
        return diskCategory;
    }

    public void setDiskCategory(String diskCategory) {
        this.diskCategory = diskCategory;
    }


    public DiskType getDiskType() {
        return diskType;
    }

    public void setDiskType(DiskType diskType) {
        this.diskType = diskType;
    }

	public boolean isBootable() {
		return bootable;
	}

	public void setBootable(boolean bootable) {
		this.bootable = bootable;
	}

	public String getImageRef() {
		return imageRef;
	}

	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	public StoragePool getPool() {
        return pool;
    }

    public void setPool(StoragePool pool) {
        this.pool = pool;
    }

    public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getImageProviderRefId() {
        return imageProviderRefId;
    }

    public void setImageProviderRefId(String imageProviderRefId) {
        this.imageProviderRefId = imageProviderRefId;
    }

    public Integer getImageProvider() {
        return imageProvider;
    }

    public void setImageProvider(Integer imageProvider) {
        this.imageProvider = imageProvider;
    }
}
