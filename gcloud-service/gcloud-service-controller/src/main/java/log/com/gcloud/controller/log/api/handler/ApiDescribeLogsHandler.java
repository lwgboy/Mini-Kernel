package com.gcloud.controller.log.api.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.log.service.ILogService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.log.model.LogAttributesType;
import com.gcloud.header.log.msg.api.ApiDescribeLogsMsg;
import com.gcloud.header.log.msg.api.ApiDescribeLogsReplyMsg;

@ApiHandler(module = Module.LOG,action = "DescribeLogs")
public class ApiDescribeLogsHandler extends MessageHandler<ApiDescribeLogsMsg, ApiDescribeLogsReplyMsg>{
	@Autowired
	private ILogService logService;
	
	@Override
	public ApiDescribeLogsReplyMsg handle(ApiDescribeLogsMsg msg) throws GCloudException {
		PageResult<LogAttributesType> response = logService.describeLogs(msg);
		ApiDescribeLogsReplyMsg replyMsg = new ApiDescribeLogsReplyMsg();
		replyMsg.init(response);
		return replyMsg;
	}
}
