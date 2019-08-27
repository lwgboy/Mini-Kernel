
package com.gcloud.header.storage.msg.node.pool;

import com.gcloud.header.NodeMessage;

public class NodeReportStoragePoolReplyMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String displayName;
    private Integer provider;
    private String storageType;
    private String poolName;
    private String hostname;
    private String categoryCode;
    private String driver;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("provider=").append(provider);
        sb.append(", storageType=").append(storageType);
        sb.append(", poolName=").append(poolName);
        sb.append(", hostname=").append(hostname);
        sb.append(", categoryCode=").append(categoryCode);
        sb.append(", displayName=").append(displayName);
        sb.append(", driver=").append(driver);
        return sb.toString();
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

}
