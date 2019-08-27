
package com.gcloud.header.storage.msg.api.pool;

import com.gcloud.header.ApiReplyMessage;

public class ApiCreateStoragePoolReplyMsg extends ApiReplyMessage {

    private static final long serialVersionUID = 1L;

    private String poolId;

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

}
