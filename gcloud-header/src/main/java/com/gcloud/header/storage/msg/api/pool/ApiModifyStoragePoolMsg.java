
package com.gcloud.header.storage.msg.api.pool;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.storage.StorageErrorCodes;

public class ApiModifyStoragePoolMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiModifyStoragePoolReplyMsg.class;
    }

    @NotBlank(message = StorageErrorCodes.INPUT_POOL_ID_ERROR)
    private String poolId;
    @NotBlank(message = StorageErrorCodes.INPUT_POOL_NAME_ERROR)
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
