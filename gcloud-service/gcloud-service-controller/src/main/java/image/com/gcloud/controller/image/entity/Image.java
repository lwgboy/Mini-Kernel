package com.gcloud.controller.image.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/11/21.
 */
@Table(name = "gc_images", jdbc = "controllerJdbcTemplate")
public class Image extends ResourceProviderEntity {

    @ID
    private String id;
    private String name;
    private Long size;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Long minDisk;
    private String owner;
    private String ownerType;
    private String tenantId;
    private Boolean disable;//true禁用false可用

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SIZE = "size";
    public static final String STATUS = "status";
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";
    public static final String MIN_DISK = "minDisk";
    public static final String OWNER = "owner";
    public static final String OWNER_TYPE = "ownerType";
    public static final String DISABLE = "disable";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
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

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public Long getMinDisk() {
        return minDisk;
    }

    public void setMinDisk(Long minDisk) {
        this.minDisk = minDisk;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateSize(Long size) {
        this.setSize(size);
        return SIZE;
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

    public String updateMinDisk(Long minDisk) {
        this.setMinDisk(minDisk);
        return MIN_DISK;
    }

    public String updateOwner(String owner) {
        this.setOwner(owner);
        return OWNER;
    }

    public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String updateOwnerType(String ownerType) {
        this.setOwnerType(ownerType);
        return OWNER_TYPE;
    }
	
	public String updateDisable(Boolean disable) {
        this.setDisable(disable);
        return DISABLE;
    }
}
