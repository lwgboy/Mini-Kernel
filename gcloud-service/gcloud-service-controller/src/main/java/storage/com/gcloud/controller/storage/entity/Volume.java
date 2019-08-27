package com.gcloud.controller.storage.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/9/21.
 */
@Table(name = "gc_volumes", jdbc = "controllerJdbcTemplate")
public class Volume extends ResourceProviderEntity {

    @ID
    private String id;
    private String displayName;
    private String status;
    private Integer size;
    private String diskType;
    private Date createdAt;
    private Date updatedAt;
    private String description;
    private String snapshotId;
    private String category;
    private boolean bootable;
    private String imageRef;
    private String userId;

    private String storageType;
    private String poolId;
    private String poolName;

    private String zoneId;
    private String tenantId;

    public static final String ID = "id";
    public static final String DISPLAY_NAME = "displayName";
    public static final String STATUS = "status";
    public static final String SIZE = "size";
    public static final String DISK_TYPE = "diskType";
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";
    public static final String DESCRIPTION = "description";
    public static final String SNAPSHOT_ID = "snapshotId";
    public static final String CATEGORY = "category";
    public static final String BOOTABLE = "bootable";
    public static final String IMAGE_REF = "imageRef";
    public static final String USER_ID = "userId";

    public static final String STORAGE_TYPE = "storageType";
    public static final String POOL_NAME = "poolName";

    public static final String ZONE_ID = "zoneId";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateDisplayName(String displayName) {
        this.setDisplayName(displayName);
        return DISPLAY_NAME;
    }

    public String updateStatus(String status) {
        this.setStatus(status);
        return STATUS;
    }

    public String updateSize(Integer size) {
        this.setSize(size);
        return SIZE;
    }

    public String updateDiskType(String diskType) {
        this.setDiskType(diskType);
        return DISK_TYPE;
    }

    public String updateCreatedAt(Date createdAt) {
        this.setCreatedAt(createdAt);
        return CREATED_AT;
    }

    public String updateUpdatedAt(Date updatedAt) {
        this.setUpdatedAt(updatedAt);
        return UPDATED_AT;
    }

    public String updateDescription(String description) {
        this.setDescription(description);
        return DESCRIPTION;
    }

    public String updateSnapshotId(String snapshotId) {
        this.setSnapshotId(snapshotId);
        return SNAPSHOT_ID;
    }

    public String updateCategory(String category) {
        this.setCategory(category);
        return CATEGORY;
    }

    public String updateBootable(boolean bootable) {
        this.setBootable(bootable);
        return BOOTABLE;
    }

    public String updateImageRef(String imageRef) {
        this.setImageRef(imageRef);
        return IMAGE_REF;
    }

    public String updateUserId(String userId) {
        this.setUserId(userId);
        return USER_ID;
    }

    public String updateStorageType(String storageType) {
        this.setStorageType(storageType);
        return STORAGE_TYPE;
    }

    public String updatePoolName(String poolName) {
        this.setPoolName(poolName);
        return POOL_NAME;
    }

    public String updateZoneId(String zoneId) {
        this.setZoneId(zoneId);
        return ZONE_ID;
    }

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
