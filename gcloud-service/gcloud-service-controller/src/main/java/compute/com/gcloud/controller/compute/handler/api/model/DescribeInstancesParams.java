package com.gcloud.controller.compute.handler.api.model;

import java.util.List;

import com.gcloud.common.model.PageParams;

public class DescribeInstancesParams extends PageParams{
	private String instanceName;
	private String status;
	private String zoneId;
	private List<String> instanceIds;
	
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public List<String> getInstanceIds() {
		return instanceIds;
	}
	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}
	
}
