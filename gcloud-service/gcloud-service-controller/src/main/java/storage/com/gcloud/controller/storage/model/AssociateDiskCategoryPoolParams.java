package com.gcloud.controller.storage.model;

public class AssociateDiskCategoryPoolParams {
	private String diskCategoryId;
	private String zoneId;
	private String poolId;
	
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
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	
	
}
