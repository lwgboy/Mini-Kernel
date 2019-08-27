package com.gcloud.controller.image.model;

import com.gcloud.common.model.PageParams;

/**
 * Created by yaowj on 2018/11/22.
 */
public class DescribeImageParams extends PageParams {

    private String imageName;
    private String imageId;
    private String status;
    private Boolean disable;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
}
