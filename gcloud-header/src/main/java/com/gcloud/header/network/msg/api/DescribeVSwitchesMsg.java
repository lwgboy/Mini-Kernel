package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

public class DescribeVSwitchesMsg extends ApiPageMessage {
	@ApiModel(description = "网络Id", require=true)
	private String vpcId;
	@ApiModel(description = "交换机Id")
	private String vSwitchId;

	@Override
	public Class replyClazz() {
		return DescribeVSwitchesReplyMsg.class;
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public String getvSwitchId() {
		return vSwitchId;
	}

	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}
}
