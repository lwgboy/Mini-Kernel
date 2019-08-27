package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CreateVpcReplyMsg extends ApiReplyMessage {
	@ApiModel(description="专有网络ID")
	private String vpcId;

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
}
