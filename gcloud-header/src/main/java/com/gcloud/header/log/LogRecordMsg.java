package com.gcloud.header.log;

import java.util.Date;

import com.gcloud.header.ApiMessage;

public class LogRecordMsg extends ApiMessage{

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String command;
    private Date startTime;
    private Date endTime;
    private String funName;
    private String curUserId;
    private String curUserName;
    private String ip;
    private String objectId;
    private String objectName;
    private String taskExpect;
    private String taskId;
    private Long timeout;
    private Byte result;
    private String finalResult;
	private String description;
	private String errorCode;
	private String remark;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getFunName() {
		return funName;
	}
	public void setFunName(String funName) {
		this.funName = funName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getTaskExpect() {
		return taskExpect;
	}
	public void setTaskExpect(String taskExpect) {
		this.taskExpect = taskExpect;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Long getTimeout() {
		return timeout;
	}
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
	public Byte getResult() {
		return result;
	}
	public void setResult(Byte result) {
		this.result = result;
	}
	public String getFinalResult() {
		return finalResult;
	}
	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCurUserId() {
		return curUserId;
	}
	public void setCurUserId(String curUserId) {
		this.curUserId = curUserId;
	}
	public String getCurUserName() {
		return curUserName;
	}
	public void setCurUserName(String curUserName) {
		this.curUserName = curUserName;
	}

}
