package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CreateExternalNetworkReplyMsg extends ApiReplyMessage{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModel(description = "外部网络 ID")
	private String networkId;
	
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

}
