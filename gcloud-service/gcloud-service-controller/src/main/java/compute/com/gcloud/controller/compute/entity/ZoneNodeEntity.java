package com.gcloud.controller.compute.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_zone_node")
public class ZoneNodeEntity {
	
	@ID
	private String id;
	private String hostname;
	private String zoneId;
	
	public static final String ID = "id";
	public static final String HOSTNAME = "hostname";
	public static final String ZONE_ID = "zoneId";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	
	public String updateId(String id) {
		this.setId(id);
		return ID;
	}
	
	public String updateHostName(String hostName) {
		this.setHostname(hostName);
		return HOSTNAME;
	}
	
	public String updateZoneId(String zoneId) {
		this.setZoneId(zoneId);
		return ZONE_ID;
	}
}
