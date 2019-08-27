package com.gcloud.controller.storage.model;

public class AssociateDiskCategoryZoneParams {
	private String diskCategoryId;
	private String zoneId;
	
	public String getDiskCategoryId() {
		return diskCategoryId;
	}
	public void setDiskCategoryId(String diskCategoryId) {
		this.diskCategoryId = diskCategoryId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
