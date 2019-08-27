package com.gcloud.header.image.msg.node;

import com.gcloud.header.NodeMessage;

public class DownloadImageMsg  extends NodeMessage {
	private String imageId;
	private String provider;//glance、gcloud
	private String stroageType;//file\rbd
	private String imagePath;//image 源路径

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getStroageType() {
		return stroageType;
	}

	public void setStroageType(String stroageType) {
		this.stroageType = stroageType;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
