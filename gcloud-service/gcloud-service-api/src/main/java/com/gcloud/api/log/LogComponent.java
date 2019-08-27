package com.gcloud.api.log;

import java.util.Date;

import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.error.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.log.enums.LogResult;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.header.ApiMessage;
import com.gcloud.header.log.LogRecordMsg;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LogComponent {
	@Autowired
	MessageBus bus;
	
	@Async
	public void logRecord(String messageId, ApiMessage message, MessageHandler handler, GCloudException ge) {
	    log.debug("LogComponent logRecord ,module=" + message.getModule() + ",action=" + message.getAction() + ",  currentThread:" + Thread.currentThread().getId());
	    GcLog gcLog = (GcLog)handler.getClass().getAnnotation(GcLog.class);
    	LogRecordMsg logMsg = new LogRecordMsg();
		logMsg.setCommand(handler.getClass().getSimpleName().substring(0, handler.getClass().getSimpleName().indexOf("$$")));
		logMsg.setCurUserId(message.getCurrentUser().getId());
		logMsg.setCurUserName(message.getCurrentUser().getLoginName());
		logMsg.setStartTime(new Date());
		logMsg.setEndTime(new Date());
		//logMsg.setFunName("/" + message.getModule() + "/" + message.getAction());
		logMsg.setFunName(gcLog.taskExpect());
		logMsg.setIp(message.getIp());
		logMsg.setModule(message.getModule());
		logMsg.setAction(message.getAction());
		logMsg.setObjectId(message.getObjectId());
		logMsg.setObjectName(message.getObjectName());
		logMsg.setRemark(message.getRemark());
		logMsg.setTaskExpect(gcLog.taskExpect());
		logMsg.setRegion("testRegion");
		
		if(null == ge) {
			logMsg.setResult(LogResult.SUCCESS.getResult());
			logMsg.setFinalResult(LogResult.SUCCESS.getResultCn());
		} else {
			logMsg.setResult(LogResult.FAIL.getResult());
			logMsg.setFinalResult(LogResult.FAIL.getResultCn());
		}
		
		if(logMsg.getResult().equals(LogResult.SUCCESS.getResult())) {
			logMsg.setDescription(gcLog.taskExpect());
		} else if(logMsg.getResult().equals(LogResult.FAIL.getResult())) {
			ErrorInfo errorInfo = new ErrorInfo(ge);
			logMsg.setErrorCode(errorInfo.getCode());
		}
		
		
		logMsg.setServiceId(messageId);
		bus.send(logMsg);
	}
}
