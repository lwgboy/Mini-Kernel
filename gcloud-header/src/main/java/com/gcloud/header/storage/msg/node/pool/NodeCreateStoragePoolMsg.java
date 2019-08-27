
package com.gcloud.header.storage.msg.node.pool;

import com.gcloud.header.NodeMessage;

public class NodeCreateStoragePoolMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String poolId;
    private String storageType;
    private String poolName;

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
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

}
