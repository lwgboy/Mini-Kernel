package com.gcloud.header.image.msg.api;

import com.gcloud.header.ApiMessage;

public class ApiImagesStatisticsMsg extends ApiMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiImagesStatisticsReplyMsg.class;
	}

}
