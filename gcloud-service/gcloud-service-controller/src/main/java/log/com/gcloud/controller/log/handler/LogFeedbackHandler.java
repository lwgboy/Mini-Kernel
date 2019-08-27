package com.gcloud.controller.log.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.log.model.LogFeedbackParams;
import com.gcloud.controller.log.service.ILogService;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.log.LogFeedbackMsg;
import com.gcloud.header.log.LogFeedbackReplyMsg;

@Handler
public class LogFeedbackHandler extends MessageHandler<LogFeedbackMsg, LogFeedbackReplyMsg>{
	@Autowired
	private ILogService logService;
	
	@Override
	public LogFeedbackReplyMsg handle(LogFeedbackMsg msg) {
		LogFeedbackParams param = new LogFeedbackParams();
		param.setCode(msg.getCode());
		param.setObjectId(msg.getObjectId());
		param.setStatus(msg.getStatus());
		param.setTaskId(msg.getTaskId());
		logService.feedback(param);
		
		return null;
	}

}
