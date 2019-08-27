package com.gcloud.core.messagebus;

import com.gcloud.header.GMessage;
import com.gcloud.header.NeedReplyMessage;
import com.gcloud.header.ReplyMessage;

public interface MessageBus {
	<REQ extends GMessage> void send(REQ msg);
	<RPLY extends ReplyMessage> RPLY call(NeedReplyMessage msg, Class<RPLY> clazz);
	ReplyMessage call(NeedReplyMessage msg);
	
}
