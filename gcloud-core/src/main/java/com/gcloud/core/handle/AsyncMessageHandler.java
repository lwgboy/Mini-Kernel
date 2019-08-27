package com.gcloud.core.handle;

import com.gcloud.header.GMessage;
import com.gcloud.header.ReplyMessage;

public abstract class AsyncMessageHandler<T extends GMessage>{
	public abstract void handle(T msg);
}
