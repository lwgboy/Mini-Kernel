package com.gcloud.header.network.msg.sdk;

import com.gcloud.header.ApiMessage;

public class SdkCreateTenantSecurityGroupMsg extends ApiMessage{
	private String securityGroupName;
	private String description;
	private String tenantId;

	@Override
	public Class replyClazz() {
		return SdkCreateTenantSecurityGroupReplyMsg.class;
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

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
}
