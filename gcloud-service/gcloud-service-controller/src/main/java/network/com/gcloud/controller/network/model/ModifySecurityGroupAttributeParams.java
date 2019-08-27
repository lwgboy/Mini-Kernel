package com.gcloud.controller.network.model;

public class ModifySecurityGroupAttributeParams {
	private String securityGroupId;
	private String securityGroupName;
	private String description;
	
	public String getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	public String getSecurityGroupName() {
		return securityGroupName;
	}
	public void setSecurityGroupName(String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
