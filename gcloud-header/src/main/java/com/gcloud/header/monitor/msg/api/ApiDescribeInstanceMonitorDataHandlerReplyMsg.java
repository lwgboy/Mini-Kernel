package com.gcloud.header.monitor.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.monitor.model.DescribeInstanceMonitorDataResponse;

public class ApiDescribeInstanceMonitorDataHandlerReplyMsg extends ApiReplyMessage {
	
	private static final long serialVersionUID = 1L;
	
	private DescribeInstanceMonitorDataResponse monitorData;

	public DescribeInstanceMonitorDataResponse getMonitorData() {
		return monitorData;
	}

	public void setMonitorData(DescribeInstanceMonitorDataResponse monitorData) {
		this.monitorData = monitorData;
	}
	
}
