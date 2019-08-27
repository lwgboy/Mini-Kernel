
package com.gcloud.header.storage.msg.api.pool;

import com.gcloud.header.ApiMessage;

public class ApiDescribeStoragePoolsMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiDescribeStoragePoolsReplyMsg.class;
    }

}
