package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeLoadBalancerTCPListenerMsg extends ApiMessage {

	@ApiModel(description = "监听器ID", require = true)
	@NotBlank(message = "0120901::监听器ID不能为空")
	private String  listenerId;

	public String getListenerId() {
		return listenerId;
	}



	public void setListenerId(String listenerId) {
		this.listenerId = listenerId;
	}



	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiDescribeLoadBalancerTCPListenerReplyMsg.class;
	}

}
