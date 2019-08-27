package com.gcloud.header.slb.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeSchedulerAttributeReplyMsg extends ApiReplyMessage {

	@ApiModel(description = "调度策略名称")
	private String scheduler;

	public String getScheduler() {
		return scheduler;
	}

	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}
	
	
}
