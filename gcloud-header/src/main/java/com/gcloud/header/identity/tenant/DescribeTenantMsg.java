package com.gcloud.header.identity.tenant;

import com.gcloud.header.ApiPageMessage;

public class DescribeTenantMsg extends ApiPageMessage{

	@Override
	public Class replyClazz() {
		return DescribeTenantReplyMsg.class;
	}

}
