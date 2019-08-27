package com.gcloud.controller.storage.model;

/**
 * Created by yaowj on 2018/9/21.
 */
public class CreateDiskParams {

    private String diskName;
    private String description;
    private Integer size;
    private String snapshotId;
    private String diskCategory;
    private String zoneId;
    
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

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
    
}
