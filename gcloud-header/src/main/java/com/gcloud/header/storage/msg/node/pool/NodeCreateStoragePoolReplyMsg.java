
package com.gcloud.header.storage.msg.node.pool;

import com.gcloud.header.NodeMessage;

public class NodeCreateStoragePoolReplyMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String poolId;

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

}
