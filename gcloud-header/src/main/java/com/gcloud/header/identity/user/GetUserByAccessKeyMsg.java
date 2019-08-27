package com.gcloud.header.identity.user;

import com.gcloud.header.NeedReplyMessage;

public class GetUserByAccessKeyMsg extends NeedReplyMessage{
	private String accessKey;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return GetUserByAccessKeyReplyMsg.class;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

}
