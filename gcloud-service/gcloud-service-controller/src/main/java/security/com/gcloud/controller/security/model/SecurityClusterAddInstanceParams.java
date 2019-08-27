package com.gcloud.controller.security.model;

import java.util.List;

public class SecurityClusterAddInstanceParams {
	private String id;
	private List<String> instanceIds;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getInstanceIds() {
		return instanceIds;
	}
	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}
}
