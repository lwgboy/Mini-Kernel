package com.gcloud.header.compute.msg.api.vm.statistics;

import com.gcloud.header.ApiMessage;

public class ApiRedundantResourceAllocateMsg extends ApiMessage{

	@Override
	public Class replyClazz() {
		return ApiRedundantResourceAllocateReplyMsg.class;
	}

}
