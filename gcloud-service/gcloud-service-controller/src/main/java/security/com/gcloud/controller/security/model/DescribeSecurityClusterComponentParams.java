package com.gcloud.controller.security.model;

import com.gcloud.common.model.PageParams;

public class DescribeSecurityClusterComponentParams extends PageParams{
	private String id;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
