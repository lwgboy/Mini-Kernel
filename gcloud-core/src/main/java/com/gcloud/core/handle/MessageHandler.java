package com.gcloud.core.handle;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.GMessage;
import com.gcloud.header.ReplyMessage;

public abstract class MessageHandler<T extends GMessage, REPY extends ReplyMessage> { //extends MessageCommand
	public abstract REPY handle(T msg) throws GCloudException;
}
