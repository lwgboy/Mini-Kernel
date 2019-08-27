package com.gcloud.header.identity.user;

import com.gcloud.header.ApiPageMessage;

public class DescribeUserMsg extends ApiPageMessage{

	@Override
	public Class replyClazz() {
		return DescribeUserReplyMsg.class;
	}

}
