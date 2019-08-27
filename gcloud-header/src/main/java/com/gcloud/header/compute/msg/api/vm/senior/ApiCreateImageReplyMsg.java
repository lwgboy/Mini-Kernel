package com.gcloud.header.compute.msg.api.vm.senior;

import com.gcloud.header.ApiReplyMessage;

/**
 * Created by yaowj on 2018/9/17.
 */
public class ApiCreateImageReplyMsg extends ApiReplyMessage {

    private String imageId;
    private String imageName;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
