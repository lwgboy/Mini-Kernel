package com.gcloud.header.log;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.Module;

public class LogFeedbackMsg extends ApiMessage{

	@Override
	public Class replyClazz() {
		return null;
	}
	
	private String taskId;
	private String objectId;
	private String status;
	private String code;
	
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
