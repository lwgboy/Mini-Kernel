package com.gcloud.controller.storage.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_storage_pool_zones", jdbc = "controllerJdbcTemplate")
public class StoragePoolZone {

    @ID
    private Integer id;
    private String storagePoolId;
    private String zoneId;

    public static final String ID = "id";
    public static final String STORAGE_POOL_ID = "storagePoolId";
    public static final String ZONE_ID = "zoneId";

    public String updateId(Integer id) {
        this.setId(id);
        return ID;
    }

    public String updateStoragePoolId(String storagePoolId) {
        this.setStoragePoolId(storagePoolId);
        return STORAGE_POOL_ID;
    }

    public String updateZoneId(String zoneId) {
        this.setZoneId(zoneId);
        return ZONE_ID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(String storagePoolId) {
        this.storagePoolId = storagePoolId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
