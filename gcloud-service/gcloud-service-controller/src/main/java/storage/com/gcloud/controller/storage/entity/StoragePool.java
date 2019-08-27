
package com.gcloud.controller.storage.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_storage_pools", jdbc = "controllerJdbcTemplate")
public class StoragePool extends ResourceProviderEntity {

    @ID
    private String id;
    private String displayName;
    private String storageType;
    private String poolName;

    private String hostname;
    private String driver;
    private String connectProtocol;

    public static final String ID = "id";
    public static final String DISPLAY_NAME = "displayName";
    public static final String STORAGE_TYPE = "storageType";
    public static final String POOL_NAME = "poolName";
    public static final String CATEGORY_ID = "categoryId";
    public static final String HOSTNAME = "hostname";

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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getConnectProtocol() {
        return connectProtocol;
    }

    public void setConnectProtocol(String connectProtocol) {
        this.connectProtocol = connectProtocol;
    }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateDisplayName(String displayName) {
        this.setDisplayName(displayName);
        return DISPLAY_NAME;
    }

    public String updatePoolName(String poolName) {
        this.setPoolName(poolName);
        return POOL_NAME;
    }

    public String updateStorageType(String storageType) {
        this.setStorageType(storageType);
        return STORAGE_TYPE;
    }

    public String updateHostname(String hostname){
        this.setHostname(hostname);
        return HOSTNAME;
    }

}
