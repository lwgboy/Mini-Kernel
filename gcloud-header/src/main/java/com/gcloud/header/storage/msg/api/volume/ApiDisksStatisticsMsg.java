package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiMessage;

public class ApiDisksStatisticsMsg extends ApiMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiDisksStatisticsReplyMsg.class;
	}

}
