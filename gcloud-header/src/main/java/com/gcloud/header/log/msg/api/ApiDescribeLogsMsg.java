package com.gcloud.header.log.msg.api;

import java.util.Date;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeLogsMsg extends ApiPageMessage{
	@ApiModel(description="用户名")
	private String userName;
	@ApiModel(description="操作功能")
	private String funName;
	@ApiModel(description="操作开始时间")
	private String startTime;
	@ApiModel(description="操作结束时间")
	private String endTime;

	@Override
	public Class replyClazz() {
		return ApiDescribeLogsReplyMsg.class;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
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

}
