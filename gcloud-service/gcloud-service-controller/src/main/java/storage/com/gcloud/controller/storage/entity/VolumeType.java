package com.gcloud.controller.storage.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/11/8.
 */
@Table(name = "gc_volume_types", jdbc = "controllerJdbcTemplate")
public class VolumeType {

    @ID
    private String id;
    private String name;
    private String storageType;
    private Date created_at;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String STORAGE_TYPE = "storageType";
    public static final String CREATED_AT = "created_at";

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateStorageType(String storageType) {
        this.setStorageType(storageType);
        return STORAGE_TYPE;
    }

    public String updateCreated_at(Date created_at) {
        this.setCreated_at(created_at);
        return CREATED_AT;
    }


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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
