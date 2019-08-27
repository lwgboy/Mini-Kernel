package com.gcloud.header.image.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaowj on 2018/9/29.
 */
public class DescribeImagesResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ImageType> image;

    public List<ImageType> getImage() {
        return image;
    }

    public void setImage(List<ImageType> image) {
        this.image = image;
    }
}
