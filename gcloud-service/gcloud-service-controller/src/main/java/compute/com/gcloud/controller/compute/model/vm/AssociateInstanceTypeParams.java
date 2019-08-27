package com.gcloud.controller.compute.model.vm;

public class AssociateInstanceTypeParams {
	private String instanceTypeId;
	private String zoneId;
	
	public String getInstanceTypeId() {
		return instanceTypeId;
	}
	public void setInstanceTypeId(String instanceTypeId) {
		this.instanceTypeId = instanceTypeId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
