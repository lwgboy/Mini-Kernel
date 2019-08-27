package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeSchedulerAttributeMsg extends ApiMessage {

	@ApiModel(description = "监听器ID或后端服务器组ID", require = true)
	@NotBlank(message = "0130201::ID不能为空")
	private String resourceId;
	@ApiModel(description = "协议")
	private String protocol;

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



	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiDescribeSchedulerAttributeReplyMsg.class;
	}

}
