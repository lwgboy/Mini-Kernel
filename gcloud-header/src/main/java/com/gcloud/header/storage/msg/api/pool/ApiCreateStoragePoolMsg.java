
package com.gcloud.header.storage.msg.api.pool;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.storage.StorageErrorCodes;

public class ApiCreateStoragePoolMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiCreateStoragePoolReplyMsg.class;
    }

    @NotBlank(message = StorageErrorCodes.INPUT_POOL_NAME_ERROR)
    private String displayName;
    @NotNull(message = StorageErrorCodes.INPUT_PROVIDER_ERROR)
    private Integer provider;
    @NotBlank(message = StorageErrorCodes.INPUT_STORAGE_TYPE_ERROR)
    private String storageType;
    @NotBlank(message = StorageErrorCodes.INPUT_POOL_NAME_ERROR)
    private String poolName;
    @NotBlank(message = StorageErrorCodes.INPUT_ZONE_ID_ERROR)
    private String zoneId;
    @NotBlank(message = StorageErrorCodes.INPUT_DISK_CATEGORY_ERROR)
    private String categoryId;
    private String hostname;
    private String driver;

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

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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
