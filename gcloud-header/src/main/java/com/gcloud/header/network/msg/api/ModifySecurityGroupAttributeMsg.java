package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ModifySecurityGroupAttributeMsg extends ApiMessage {
	@NotBlank(message = "0040201")
	@ApiModel(description = "安全组ID", require = true)
	private String securityGroupId;
	@ApiModel(description = "安全组名称")
	@Length(max = 255, message = "0040203::安全组名称长度不能大于255")
	private String securityGroupName;
	@ApiModel(description = "描述")
	@Length(max = 255, message = "0040204::描述长度不能大于255")
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

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

}
