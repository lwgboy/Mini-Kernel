package com.gcloud.header.slb.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CreateLoadBalancerReplyMsg extends ApiReplyMessage {
	
	@ApiModel(description = "实例ID")
	private String loadBalancerId;
	@ApiModel(description = "服务地址")
	private String address;
	
	public String getLoadBalancerId() {
		return loadBalancerId;
	}
	public void setLoadBalancerId(String loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	

}
