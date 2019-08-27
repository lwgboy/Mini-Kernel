package com.gcloud.controller.network.model.workflow;

public class DeleteNetcardFlowCommandRes {

	private String subnetId;
	private String ipAddress;
	private String userId;

	public String getSubnetId() {
		return subnetId;
	}

	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
