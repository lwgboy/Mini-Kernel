package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.controller.ControllerProperty;
import com.gcloud.header.network.model.PermissionTypes;

public class DescribeSecurityGroupAttributeReplyMsg extends ApiReplyMessage {
	@ApiModel(description = "描述")
	private String description;
	@ApiModel(description = "安全组名称")
	private String securityGroupName;
	@ApiModel(description = "安全组Id")
	private String securityGroupId;
	@ApiModel(description = "区域")
	private String regionId = ControllerProperty.REGION_ID;
	@ApiModel(description = "安全组规则集合类型")
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
