package com.gcloud.controller.storage.model;

import com.gcloud.common.model.PageParams;

public class DescribeDiskCategoriesParams extends PageParams{
	private String zoneId;

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
