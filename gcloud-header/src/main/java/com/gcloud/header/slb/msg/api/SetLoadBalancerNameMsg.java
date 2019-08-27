package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;


public class SetLoadBalancerNameMsg extends ApiMessage {
	
	@ApiModel(description = "负载均衡ID", require = true)
	@NotBlank(message = "0110202::ID不能为空")
	private String  loadBalancerId;
	@ApiModel(description = "名称", require = true)
	@Length(min=2, max = 20, message = "0110203::名称长度为[2,20]")
	private String loadBalancerName;

	public String getLoadBalancerId() {
		return loadBalancerId;
	}

	public void setLoadBalancerId(String loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}

	public String getLoadBalancerName() {
		return loadBalancerName;
	}

	public void setLoadBalancerName(String loadBalancerName) {
		this.loadBalancerName = loadBalancerName;
	}

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return SetLoadBalancerNameReplyMsg.class;
	}

}
