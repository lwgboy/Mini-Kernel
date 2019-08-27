package com.gcloud.controller.network.model;

import com.gcloud.header.network.model.PermissionTypes;

public class DescribeSecurityGroupAttributeResponse {
	private String description;
	private String securityGroupName;
	private String securityGroupId;
	private String regionId;
	private PermissionTypes permissions;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSecurityGroupName() {
		return securityGroupName;
	}
	public void setSecurityGroupName(String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}
	public String getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public PermissionTypes getPermissions() {
		return permissions;
	}
	public void setPermissions(PermissionTypes permissions) {
		this.permissions = permissions;
	}
	
}
