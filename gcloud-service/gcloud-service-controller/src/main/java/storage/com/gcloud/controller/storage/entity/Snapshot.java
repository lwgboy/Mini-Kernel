package com.gcloud.controller.storage.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/11/7.
 */
@Table(name = "gc_snapshots", jdbc = "controllerJdbcTemplate")
public class Snapshot extends ResourceProviderEntity {

    @ID
    private String id;
    private String displayName;
    private String displayDescription;
    private Integer volumeSize;
    private String volumeId;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String userId;

    private String storageType;
    private String poolName;
    private String tenantId;

    public static final String ID = "id";
    public static final String DISPLAY_NAME = "displayName";
    public static final String DISPLAY_DESCRIPTION = "displayDescription";
    public static final String VOLUME_SIZE = "volumeSize";
    public static final String VOLUME_ID = "volumeId";
    public static final String STATUS = "status";
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";
    public static final String USER_ID = "userId";

    public static final String STORAGE_TYPE = "storageType";
    public static final String POOL_NAME = "poolName";

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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDisplayDescription() {
        return displayDescription;
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
    }

    public Integer getVolumeSize() {
        return volumeSize;
    }

    public void setVolumeSize(Integer volumeSize) {
        this.volumeSize = volumeSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
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

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateDisplayName(String displayName) {
        this.setDisplayName(displayName);
        return DISPLAY_NAME;
    }

    public String updateDisplayDescription(String displayDescription) {
        this.setDisplayDescription(displayDescription);
        return DISPLAY_DESCRIPTION;
    }

    public String updateVolumeSize(Integer volumeSize) {
        this.setVolumeSize(volumeSize);
        return VOLUME_SIZE;
    }

    public String updateVolumeId(String volumeId) {
        this.setVolumeId(volumeId);
        return VOLUME_ID;
    }

    public String updateStatus(String status) {
        this.setStatus(status);
        return STATUS;
    }

    public String updateCreatedAt(Date createdAt) {
        this.setCreatedAt(createdAt);
        return CREATED_AT;
    }

    public String updateUpdatedAt(Date updatedAt) {
        this.setUpdatedAt(updatedAt);
        return UPDATED_AT;
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

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
