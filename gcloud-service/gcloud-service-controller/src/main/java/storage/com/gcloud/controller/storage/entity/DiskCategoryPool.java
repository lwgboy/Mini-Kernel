package com.gcloud.controller.storage.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_disk_category_pools", jdbc = "controllerJdbcTemplate")
public class DiskCategoryPool {

    @ID
    private Integer id;
    private String diskCategoryId;
    private String zoneId;
    private String storagePoolId;

    public static final String ID = "id";
    public static final String DISK_CATEGORY_ID = "diskCategoryId";
    public static final String ZONE_ID = "zoneId";
    public static final String STORAGE_POOL_ID = "storagePoolId";

    public String updateId(Integer id) {
        this.setId(id);
        return ID;
    }

    public String updateDiskCategoryId(String diskCategoryId) {
        this.setDiskCategoryId(diskCategoryId);
        return DISK_CATEGORY_ID;
    }

    public String updateZoneId(String zoneId) {
        this.setZoneId(zoneId);
        return ZONE_ID;
    }

    public String updateStoragePoolId(String storagePoolId) {
        this.setStoragePoolId(storagePoolId);
        return STORAGE_POOL_ID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiskCategoryId() {
        return diskCategoryId;
    }

    public void setDiskCategoryId(String diskCategoryId) {
        this.diskCategoryId = diskCategoryId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(String storagePoolId) {
        this.storagePoolId = storagePoolId;
    }
}
