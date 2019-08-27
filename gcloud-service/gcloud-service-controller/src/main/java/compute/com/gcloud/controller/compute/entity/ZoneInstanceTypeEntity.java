package com.gcloud.controller.compute.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_zones_instance_type")
public class ZoneInstanceTypeEntity {
	@ID
	private int id;
	private String zoneId;
	private String instanceTypeId;
	
	public static final String ID = "id";
	public static final String ZONE_ID = "zoneId";
	public static final String INSTANCE_TYPE_ID = "instanceTypeId";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getInstanceTypeId() {
		return instanceTypeId;
	}
	public void setInstanceTypeId(String instanceTypeId) {
		this.instanceTypeId = instanceTypeId;
	}
}
