package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiDeleteMessage;
import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class DeleteLoadBalancerMsg extends ApiDeleteMessage {
	
	@ApiModel(description = "负载均衡ID")
	@NotBlank(message = "0110301::ID不能为空")
	private String  loadBalancerId;

	public String getLoadBalancerId() {
		return loadBalancerId;
	}

	public void setLoadBalancerId(String loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiReplyMessage.class;
	}

}
