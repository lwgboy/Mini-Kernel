package com.gcloud.core.messagebus;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.header.GMessage;
import com.gcloud.header.NeedReplyMessage;
import com.gcloud.header.ReplyMessage;
@Service
public class MessageBusImpl implements MessageBus{
	@Autowired
    private AmqpTemplate rabbitTemplate;
	
	@Override
	public <REQ extends GMessage> void send(REQ msg) {
		// TODO Auto-generated method stub
		rabbitTemplate.convertAndSend(msg.getServiceId() + "_async", msg);
	}
	
	@Override
	public <RPLY extends ReplyMessage> RPLY call(NeedReplyMessage msg, Class<RPLY> clazz) {
		// TODO Auto-generated method stub
		Object obj=rabbitTemplate.convertSendAndReceive(msg.getServiceId(),msg);
		return clazz.cast(obj);
	}

	@Override
	public ReplyMessage call(NeedReplyMessage msg) {
		return call(msg, msg.replyClazz());
	}
}
