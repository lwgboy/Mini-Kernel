package com.gcloud.header.identity.user;

import com.gcloud.header.ReplyMessage;

public class GetUserByAccessKeyReplyMsg extends ReplyMessage{
	private String userId;
	private String secretKey;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
