package com.gcloud.header.identity.user;

import java.io.Serializable;
import java.util.Date;

public class UserModel implements Serializable{
	private String id;
	private String loginName;
	private Boolean gender;//false男true女
	private String email;
	private String mobile;
	private String roleId;//超级管理员admin、租户管理员、普通用户
	private Boolean disable;//true禁用false可用
	private String realName;
	private String createTime;
	private String creater;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
}
