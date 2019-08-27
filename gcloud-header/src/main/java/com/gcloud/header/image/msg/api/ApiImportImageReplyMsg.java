package com.gcloud.header.image.msg.api;

import com.gcloud.header.ApiReplyMessage;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiImportImageReplyMsg extends ApiReplyMessage {

    private static final long serialVersionUID = 1L;

    private String imageId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
