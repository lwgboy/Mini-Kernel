package com.gcloud.header.identity.user;


import java.util.List;

import com.gcloud.header.ApiReplyMessage;

public class GetUserReplyMsg extends ApiReplyMessage {
	private String id;
	private String loginName;
	private Boolean gender;//false男true女
	private String email;
	private String mobile;
	private String roleId;//超级管理员admin、租户管理员、普通用户
	private Boolean disable;//true禁用false可用
	private String realName;
	private String domain;
	List<String>  tenants;
	
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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public List<String> getTenants() {
		return tenants;
	}
	public void setTenants(List<String> tenants) {
		this.tenants = tenants;
	}
	
}
