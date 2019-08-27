package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class DescribeSecurityGroupAttributeMsg extends ApiMessage {
	
	@ApiModel(description = "安全组Id", require = true)
	@NotBlank(message = "0040701")
	private String securityGroupId;
	@ApiModel(description = "安全组方向")
	private String direction;
	
	public String getSecurityGroupId() {
		return securityGroupId;
	}


	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}


	public String getDirection() {
		return direction;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}


	@Override
	public Class replyClazz() {
		return DescribeSecurityGroupAttributeReplyMsg.class;
	}

}
