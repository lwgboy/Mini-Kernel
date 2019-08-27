package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CreateNetworkInterfaceReplyMsg extends ApiReplyMessage {

	private static final long serialVersionUID = 1L;

	@ApiModel(description = "网卡ID")
	private String networkInterfaceId;

	public String getNetworkInterfaceId() {
		return networkInterfaceId;
	}

	public void setNetworkInterfaceId(String networkInterfaceId) {
		this.networkInterfaceId = networkInterfaceId;
	}
	
}
