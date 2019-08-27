package com.gcloud.controller.log.service;

import com.gcloud.controller.log.entity.Log;
import com.gcloud.controller.log.model.LogFeedbackParams;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.GMessage;
import com.gcloud.header.ReplyMessage;
import com.gcloud.header.log.LogRecordMsg;
import com.gcloud.header.log.model.LogAttributesType;
import com.gcloud.header.log.msg.api.ApiDescribeLogsMsg;

import java.util.Date;

public interface ILogService {
	Long save(Log log);
	
	void recordLog(GMessage message, MessageHandler handler, GCloudException ge, Date startTime);
	
	void recordMultiLog(GMessage message, MessageHandler handler, GCloudException ge, Date startTime, ReplyMessage reply);
	
	void feedback(LogFeedbackParams params);

    void feedbackAsync(LogFeedbackParams params);
	
	void logRecord(LogRecordMsg msg);
	
	PageResult<LogAttributesType> describeLogs(ApiDescribeLogsMsg msg);
}
