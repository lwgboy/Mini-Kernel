package com.gcloud.controller.log.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.log.service.ILogService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.log.LogRecordMsg;
import com.gcloud.header.log.LogRecordReplyMsg;

@Handler
public class LogRecordHandler extends MessageHandler<LogRecordMsg, LogRecordReplyMsg>{
	@Autowired
	private ILogService logService;
	
	@Override
	public LogRecordReplyMsg handle(LogRecordMsg msg) throws GCloudException {
		logService.logRecord(msg);
		return null;
	}

}
