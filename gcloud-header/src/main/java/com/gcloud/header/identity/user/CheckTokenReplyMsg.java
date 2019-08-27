package com.gcloud.header.identity.user;

import com.gcloud.header.ReplyMessage;

public class CheckTokenReplyMsg extends ReplyMessage{
	private String userId;
	private Long expressTime;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getExpressTime() {
		return expressTime;
	}
	public void setExpressTime(Long expressTime) {
		this.expressTime = expressTime;
	}
}
