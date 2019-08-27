package com.gcloud.controller.compute.model.node;

public class AttachNodeParams {
	private String hostname;
	private String zoneId;
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
