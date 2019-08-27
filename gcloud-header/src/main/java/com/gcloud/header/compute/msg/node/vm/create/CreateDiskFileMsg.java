package com.gcloud.header.compute.msg.node.vm.create;

import com.gcloud.header.NodeMessage;

public class CreateDiskFileMsg extends NodeMessage {
	private static final long serialVersionUID = 1L;
	
	private String imageId;
	private String volumeId;
	private Integer storageType;
	private int systemSize;
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getVolumeId() {
		return volumeId;
	}
	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}
	public Integer getStorageType() {
		return storageType;
	}
	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}
	public int getSystemSize() {
		return systemSize;
	}
	public void setSystemSize(int systemSize) {
		this.systemSize = systemSize;
	}
}
