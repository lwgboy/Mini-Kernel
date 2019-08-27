package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.network.model.DescribeVRoutersResponse;

public class DescribeVRoutersMsg  extends ApiPageMessage{
	

	@Override
	public Class replyClazz() {
		return DescribeVRoutersReplyMsg.class;
	}

}
