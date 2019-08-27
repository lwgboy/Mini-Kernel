package com.gcloud.header.image.msg.api;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.controller.ControllerProperty;
import com.gcloud.header.image.model.DescribeImagesResponse;
import com.gcloud.header.image.model.ImageType;

import java.util.List;

/**
 * Created by yaowj on 2018/9/29.
 */
public class ApiDescribeImagesReplyMsg extends PageReplyMessage<ImageType> {

    private static final long serialVersionUID = 1L;

    private DescribeImagesResponse images;
    private String regionId = ControllerProperty.REGION_ID;

    @Override
    public void setList(List<ImageType> list) {
        images = new DescribeImagesResponse();
        images.setImage(list);
    }

    public DescribeImagesResponse getImages() {
        return images;
    }

    public void setImages(DescribeImagesResponse images) {
        this.images = images;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
