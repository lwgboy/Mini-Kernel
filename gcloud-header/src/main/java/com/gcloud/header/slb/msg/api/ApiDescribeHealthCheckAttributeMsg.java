package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeHealthCheckAttributeMsg extends ApiMessage{
	
	@ApiModel(description = "监听器ID或后端服务器组ID", require = true)
	@NotBlank(message = "0150101::监听器ID或后端服务器组ID不能为空")
	private String resourceId;
	@ApiModel(description = "监听器协议，取值：TCP |HTTP | HTTPS")
	private String protocol;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiDescribeHealthCheckAttributeReplyMsg.class;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
