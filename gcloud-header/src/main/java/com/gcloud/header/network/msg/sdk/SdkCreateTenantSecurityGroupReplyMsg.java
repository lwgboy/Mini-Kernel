package com.gcloud.header.network.msg.sdk;

import com.gcloud.header.ApiReplyMessage;

public class SdkCreateTenantSecurityGroupReplyMsg extends ApiReplyMessage{
	private String securityGroupId;

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
}
