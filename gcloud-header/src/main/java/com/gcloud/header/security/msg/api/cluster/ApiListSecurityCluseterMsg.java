package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiPageMessage;

public class ApiListSecurityCluseterMsg extends ApiPageMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiListSecurityCluseterReplyMsg.class;
	}

}
