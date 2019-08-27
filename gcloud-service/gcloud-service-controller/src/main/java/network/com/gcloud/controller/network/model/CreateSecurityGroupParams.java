package com.gcloud.controller.network.model;

public class CreateSecurityGroupParams {
	private String securityGroupName;
	private String description;
	private String curUserId;
	private Integer provider;
	private boolean defaultSg = false;

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
	public String getCurUserId() {
		return curUserId;
	}
	public void setCurUserId(String curUserId) {
		this.curUserId = curUserId;
	}

	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}
	public boolean isDefaultSg() {
		return defaultSg;
	}
	public void setDefaultSg(boolean defaultSg) {
		this.defaultSg = defaultSg;
	}
}
