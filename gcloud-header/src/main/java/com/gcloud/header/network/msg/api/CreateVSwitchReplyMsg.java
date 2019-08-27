package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CreateVSwitchReplyMsg extends ApiReplyMessage {
	@ApiModel(description = "交换机Id")
	private String vSwitchId;

	public String getvSwitchId() {
		return vSwitchId;
	}

	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}
	
}
