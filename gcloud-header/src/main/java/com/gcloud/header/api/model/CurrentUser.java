package com.gcloud.header.api.model;

import java.io.Serializable;
import java.util.List;

public class CurrentUser implements Serializable {
	private static final long             serialVersionUID = 1L;

    private String                          id;
    private String                        loginName;
    private String                        realName;
    private String                        email;
    private String                        mobile;
    private String role;
    private String defaultTenant;
    private List<String> userTenants;
    
    public CurrentUser() {}
    
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
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDefaultTenant() {
		return defaultTenant;
	}
	public void setDefaultTenant(String defaultTenant) {
		this.defaultTenant = defaultTenant;
	}
	public List<String> getUserTenants() {
		return userTenants;
	}
	public void setUserTenants(List<String> userTenants) {
		this.userTenants = userTenants;
	}
}
