
package com.gcloud.header.image.msg.api;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiGenDownloadMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiGenDownloadReplyMsg.class;
    }

    @ApiModel(description = "镜像ID")
    private String imageId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

}
