
package com.gcloud.header.storage.msg.api.pool;

import com.gcloud.header.ApiReplyMessage;

public class ApiCreateDiskCategoryReplyMsg extends ApiReplyMessage {

    private static final long serialVersionUID = 1L;

    private String typeId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
