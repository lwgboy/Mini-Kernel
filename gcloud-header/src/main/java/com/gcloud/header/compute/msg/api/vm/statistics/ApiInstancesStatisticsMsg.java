package com.gcloud.header.compute.msg.api.vm.statistics;

import com.gcloud.header.ApiMessage;

public class ApiInstancesStatisticsMsg extends ApiMessage{

	@Override
	public Class replyClazz() {
		return ApiInstancesStatisticsReplyMsg.class;
	}

}
