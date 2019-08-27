package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiPageMessage;

public class DescribeSecurityGroupsMsg  extends ApiPageMessage{

	@Override
	public Class replyClazz() {
		return DescribeSecurityGroupsReplyMsg.class;
	}
	
}
