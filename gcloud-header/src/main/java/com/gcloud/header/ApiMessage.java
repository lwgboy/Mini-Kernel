package com.gcloud.header;

import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.controller.ControllerProperty;

public abstract class ApiMessage extends NeedReplyMessage {
	private String module;
	private String action;
	private String curUserId;
	private CurrentUser currentUser;
	private String ip = "127.0.0.1";//操作人IP
	private String region = ControllerProperty.REGION_ID;
	private String remark = "test备注";
	
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public CurrentUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}
	public String getCurUserId() {
		return curUserId;
	}
	public void setCurUserId(String curUserId) {
		this.curUserId = curUserId;
	}
}
