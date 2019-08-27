package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CreateSecurityGroupReplyMsg extends ApiReplyMessage {
	@ApiModel(description = "安全组Id")
	private String securityGroupId;

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	
	
}
