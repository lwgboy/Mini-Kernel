package com.gcloud.controller.network.model.workflow;

import com.gcloud.header.api.model.CurrentUser;

public class CreatePortFlowCommandReq {

	private String subnetId;
	private String ipAddress;
	private CurrentUser createUser;
	private String securityGroupId;

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

	public CurrentUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(CurrentUser createUser) {
		this.createUser = createUser;
	}

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
}
