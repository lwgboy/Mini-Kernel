package com.gcloud.header.slb.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiCreateVServerGroupReplyMsg extends ApiReplyMessage {
	
	@ApiModel(description = "虚拟服务器组ID")
	private String vServerGroupId;

	public String getvServerGroupId() {
		return vServerGroupId;
	}

	public void setvServerGroupId(String vServerGroupId) {
		this.vServerGroupId = vServerGroupId;
	}
}
