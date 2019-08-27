
package com.gcloud.header.storage.msg.api.pool;

import com.gcloud.header.ApiMessage;

public class ApiDescribeDiskCategoriesMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiDescribeDiskCategoriesReplyMsg.class;
    }

    private String zoneId;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

}
