package com.gcloud.header.monitor.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.monitor.model.DescribeDiskMonitorDataResponse;

public class ApiDescribeDiskMonitorDataHandlerReplyMsg extends ApiReplyMessage {
	
	private static final long serialVersionUID = 1L;
	
	private DescribeDiskMonitorDataResponse monitorData;

	public DescribeDiskMonitorDataResponse getMonitorData() {
		return monitorData;
	}

	public void setMonitorData(DescribeDiskMonitorDataResponse monitorData) {
		this.monitorData = monitorData;
	}
	
}
