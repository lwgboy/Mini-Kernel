package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class DeleteSecurityGroupMsg extends ApiMessage {
	@ApiModel(description = "安全组Id", require = true)
	@NotBlank(message = "0040401")
	private String securityGroupId;
	
	public String getSecurityGroupId() {
		return securityGroupId;
	}


	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}


	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
}
