
package com.gcloud.header.storage.model;

import java.io.Serializable;

import com.gcloud.framework.db.jdbc.annotation.TableField;

public class StoragePoolModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("display_name")
    private String displayName;
    @TableField("provider")
    private Integer provider;
    @TableField("storage_type")
    private String storageType;
    @TableField("pool_name")
    private String poolName;

    @TableField("hostname")
    private String hostname;
    @TableField("driver")
    private String driver;

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

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
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

}
