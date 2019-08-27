package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiRoutersStatisticsReplyMsg extends ApiReplyMessage {
	@ApiModel(description = "vpc总数量")
	private int allNum;

	public int getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}
	
}
