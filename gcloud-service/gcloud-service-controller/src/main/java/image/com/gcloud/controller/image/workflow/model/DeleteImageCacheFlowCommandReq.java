package com.gcloud.controller.image.workflow.model;

import com.gcloud.controller.image.entity.ImageStore;

public class DeleteImageCacheFlowCommandReq {
	private ImageStore repeatParams;
	private String imageId;

	public ImageStore getRepeatParams() {
		return repeatParams;
	}

	public void setRepeatParams(ImageStore repeatParams) {
		this.repeatParams = repeatParams;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
}
