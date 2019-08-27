package com.gcloud.header.log.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gcloud.header.GcloudConstants;
import com.gcloud.header.api.ApiModel;

public class LogAttributesType implements Serializable {
    
    @ApiModel(description = "日志ID")
    private Long id;
    @ApiModel(description = "用户ID")
	private String userId;
	@ApiModel(description = "用户名")
    private String userName;
	@ApiModel(description = "操作功能")
    private String funName;
	@ApiModel(description = "对象ID")
    private String objectId;
	@ApiModel(description = "操作对象")
    private String objectName;
	@ApiModel(description = "操作状态")
    private Byte result;
	@ApiModel(description = "功能描述")
    private String description;
	@ApiModel(description = "操作IP")
    private String ip;
	@ApiModel(description = "操作开始时间")
	@JsonFormat(timezone = GcloudConstants.DEFAULT_TIMEZONE, pattern = GcloudConstants.DEFAULT_DATEFORMAT)
    private Date startTime;
	@ApiModel(description = "操作结束时间")
	@JsonFormat(timezone = GcloudConstants.DEFAULT_TIMEZONE, pattern = GcloudConstants.DEFAULT_DATEFORMAT)
    private Date endTime;
	@ApiModel(description = "错误码")
    private String errorCode;
	@ApiModel(description = "任务ID")
    private String taskId;
    //备注信息，如记录操作原因
	@ApiModel(description = "操作原因")
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
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
