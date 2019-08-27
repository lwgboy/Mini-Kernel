package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CreateVRouterReplyMsg extends ApiReplyMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description = "路由器Id")
	private String vRouterId;

	public String getvRouterId() {
		return vRouterId;
	}

	public void setvRouterId(String vRouterId) {
		this.vRouterId = vRouterId;
	}
	
}
