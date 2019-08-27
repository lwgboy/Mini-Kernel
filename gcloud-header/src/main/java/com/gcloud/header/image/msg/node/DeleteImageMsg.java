package com.gcloud.header.image.msg.node;

import com.gcloud.header.NodeMessage;

public class DeleteImageMsg extends NodeMessage {
	private String imageId;
	private String storeTarget;
	private String storeType;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getStoreTarget() {
		return storeTarget;
	}

	public void setStoreTarget(String storeTarget) {
		this.storeTarget = storeTarget;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	
	
}
