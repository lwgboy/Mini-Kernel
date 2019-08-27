package com.gcloud.controller.compute.workflow.model.vm;

public class CheckAndDistributeImageFlowCommandReq {
	private String imageId;
	private String zoneId;
	private String systemDiskCategory;//Category id
	private String createHost;
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getSystemDiskCategory() {
		return systemDiskCategory;
	}
	public void setSystemDiskCategory(String systemDiskCategory) {
		this.systemDiskCategory = systemDiskCategory;
	}
	public String getCreateHost() {
		return createHost;
	}
	public void setCreateHost(String createHost) {
		this.createHost = createHost;
	}
	
	
}
