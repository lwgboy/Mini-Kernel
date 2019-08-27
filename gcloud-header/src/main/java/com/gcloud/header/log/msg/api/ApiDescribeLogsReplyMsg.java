package com.gcloud.header.log.msg.api;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.log.model.DescribeLogAttributesTypeResponse;
import com.gcloud.header.log.model.LogAttributesType;

public class ApiDescribeLogsReplyMsg extends PageReplyMessage<LogAttributesType>{
	private DescribeLogAttributesTypeResponse logs;

	@Override
	public void setList(List<LogAttributesType> list) {
		logs = new DescribeLogAttributesTypeResponse();
		logs.setLog(list);
	}

	public DescribeLogAttributesTypeResponse getLogs() {
		return logs;
	}

	public void setLogs(DescribeLogAttributesTypeResponse logs) {
		this.logs = logs;
	}

}
