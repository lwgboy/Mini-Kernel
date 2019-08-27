package com.gcloud.core.handle;

import javax.servlet.http.HttpServletRequest;

import com.gcloud.header.GMessage;
import com.gcloud.header.ReplyMessage;

public abstract class RequestHandler<T extends GMessage,REPY extends ReplyMessage> extends MessageHandler<T, REPY> {
	HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
