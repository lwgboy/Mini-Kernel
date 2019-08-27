package com.gcloud.header.compute.msg.api.vm.statistics;

import com.gcloud.header.ApiMessage;

public class ApiInstancesStatisticsByZoneMsg extends ApiMessage{

	@Override
	public Class replyClazz() {
		return ApiInstancesStatisticsByZoneReplyMsg.class;
	}

}
