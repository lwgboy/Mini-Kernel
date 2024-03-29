package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;

public class ZoneInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String zoneId;
	private String zoneName;
	
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
}
