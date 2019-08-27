package com.gcloud.header.network.model;

import java.io.Serializable;

import com.gcloud.header.api.ApiModel;

public class SecurityGroupItemType  implements Serializable{
	@ApiModel(description = "安全组Id")
	private String securityGroupId;
	@ApiModel(description = "安全组名称")
	private String securityGroupName;
	@ApiModel(description = "安全组描述")
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
