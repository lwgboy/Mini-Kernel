package com.gcloud.header.monitor.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;

public class ApiDescribeInstanceMonitorDataHandlerMsg extends ApiMessage {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "实例ID不能为空")
	private String  instanceId; //实例id
	
	@NotBlank(message = "起始时间不能为空")
	private String startTime; //起始时间，格式示例：2019-02-21 15:51:10
	
	@NotBlank(message = "结束时间不能为空")
	private String endTime; //结束时间，格式示例：2019-02-21 15:51:10
	
	private String period; //监控数据的频率，60 秒/600秒/3600秒 默认 60 秒
	
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiReplyMessage.class;
	}

}
