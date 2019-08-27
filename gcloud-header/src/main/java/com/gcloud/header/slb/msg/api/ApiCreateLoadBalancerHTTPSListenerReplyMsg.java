package com.gcloud.header.slb.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiCreateLoadBalancerHTTPSListenerReplyMsg extends ApiReplyMessage {

	@ApiModel(description = "监听器ID")
	private String listenerId;

	public String getListenerId() {
		return listenerId;
	}

	public void setListenerId(String listenerId) {
		this.listenerId = listenerId;
	}
	
	
}
