package com.gcloud.controller.log.entity;

import java.util.Date;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_log",jdbc="controllerJdbcTemplate")
public class Log {
	 @ID
	 private Long id;
	 private String userId;
     private String userName;
     private String funName;
     private String objectId;
     private Byte result;
     private String description;
     private String ip;
     private Date startTime;
     private Date endTime;
     private String errorCode;
     private String taskId;
     private String command;
     private Integer percent;
     private String finalResult;
     private String taskExpect;
     private String objectName;
     private Long timeout;
     //备注信息，如记录操作原因
     private String remark;
     
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public Byte getResult() {
		return result;
	}
	public void setResult(Byte result) {
		this.result = result;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Integer getPercent() {
		return percent;
	}
	public void setPercent(Integer percent) {
		this.percent = percent;
	}
	public String getFinalResult() {
		return finalResult;
	}
	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}
	public String getTaskExpect() {
		return taskExpect;
	}
	public void setTaskExpect(String taskExpect) {
		this.taskExpect = taskExpect;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getTimeout() {
		return timeout;
	}
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
}