package com.gcloud.header.image.msg.node;

import com.gcloud.header.NodeMessage;

public class DownloadImageReplyMsg  extends NodeMessage{
	private String imageId;
	private String storeTarget;

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
}
