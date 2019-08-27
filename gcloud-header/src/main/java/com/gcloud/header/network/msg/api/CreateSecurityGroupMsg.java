package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class CreateSecurityGroupMsg extends ApiMessage {
	@ApiModel(description = "安全组名称", require = true)
	@NotBlank(message = "0040101")
	@Length(max=255, message="0040102")
	private String securityGroupName;
	@ApiModel(description = "描述")
	@Length(max=255, message="0040103")
	private String description;
	@Override
	public Class replyClazz() {
		return CreateSecurityGroupReplyMsg.class;
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
