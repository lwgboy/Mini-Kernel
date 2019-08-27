package com.gcloud.header.identity.user;

import com.gcloud.header.NeedReplyMessage;

public class CheckTokenMsg extends NeedReplyMessage {
	private String token;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return CheckTokenReplyMsg.class;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
