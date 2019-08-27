package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiMessage;

public class ApiRoutersStatisticsMsg extends ApiMessage{

	@Override
	public Class replyClazz() {
		return ApiRoutersStatisticsReplyMsg.class;
	}

}
