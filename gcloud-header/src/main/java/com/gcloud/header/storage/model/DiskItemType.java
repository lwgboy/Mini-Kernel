package com.gcloud.header.storage.model;

import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.controller.ControllerProperty;

import java.io.Serializable;

/**
 * Created by yaowj on 2018/9/29.
 */
public class DiskItemType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModel(description = "创建时间")
    @TableField("created_at")
    private String creationTime;
    @ApiModel(description = "描述")
    private String description;
    @ApiModel(description = "设备")
    private String device;
    @ApiModel(description = "磁盘ID")
    @TableField("id")
    private String diskId;
    @ApiModel(description = "磁盘名称")
    @TableField("display_name")
    private String diskName;
    @ApiModel(description = "镜像ID")
    private String imageId;
    @ApiModel(description = "类别")
    private String category;
    @ApiModel(description = "实例ID")
    @TableField("instance_uuid")
    private String instanceId;
    @ApiModel(description = "可用区ID")
    private String regionId = ControllerProperty.REGION_ID;
    @ApiModel(description = "容量")
    private Integer size;
    @ApiModel(description = "快照源ID")
    private String sourceSnapshotId;
    @ApiModel(description = "状态")
    private String status;
    @ApiModel(description = "磁盘类型")
    @TableField("disk_type")
    private String type;
    @ApiModel(description = "区域ID")
    private String zoneId;

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSourceSnapshotId() {
        return sourceSnapshotId;
    }

    public void setSourceSnapshotId(String sourceSnapshotId) {
        this.sourceSnapshotId = sourceSnapshotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

}
