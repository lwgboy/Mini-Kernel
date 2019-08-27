package com.gcloud.controller.storage.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_zone_disk_categories", jdbc = "controllerJdbcTemplate")
public class DiskCategoryZone {
	
	@ID
	private String id;
	private String zoneId;
	private String diskCategoryId;

	public static final String ID = "id";
	public static final String ZONE_ID = "zoneId";
	public static final String DISK_CATEGORY_ID = "diskCategoryId";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getDiskCategoryId() {
		return diskCategoryId;
	}
	public void setDiskCategoryId(String diskCategoryId) {
		this.diskCategoryId = diskCategoryId;
	}
}
