package com.gcloud.header.storage.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gcloud.framework.db.jdbc.annotation.TableField;

/**
 * Created by yaowj on 2018/11/7.
 */
public class SnapshotType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String snapshotId;
    @TableField("display_name")
    private String snapshotName;
    @TableField("display_description")
    private String description;
    @TableField("volume_id")
    private String sourceDiskId;
    @TableField("created_at")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;
    private String status;
    @TableField("volume_size")
    private String sourceDiskSize;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceDiskId() {
        return sourceDiskId;
    }

    public void setSourceDiskId(String sourceDiskId) {
        this.sourceDiskId = sourceDiskId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceDiskSize() {
        return sourceDiskSize;
    }

    public void setSourceDiskSize(String sourceDiskSize) {
        this.sourceDiskSize = sourceDiskSize;
    }
}
