package com.gcloud.controller.security.entity;

import java.util.List;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_security_cluster")
public class SecurityClusterList {
	@ID
	private String id;
	private String name;
	private String protectionNetCidr;
	private String state;
	private String stateCnName;
	private String createUser;
	private String createUserName;
	private String createTime;
	private List<SecurityClusterComponent> securityClusterComponents;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProtectionNetCidr() {
		return protectionNetCidr;
	}
	public void setProtectionNetCidr(String protectionNetCidr) {
		this.protectionNetCidr = protectionNetCidr;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateCnName() {
		return stateCnName;
	}
	public void setStateCnName(String stateCnName) {
		this.stateCnName = stateCnName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public List<SecurityClusterComponent> getSecurityClusterComponents() {
		return securityClusterComponents;
	}
	public void setSecurityClusterComponents(List<SecurityClusterComponent> securityClusterComponents) {
		this.securityClusterComponents = securityClusterComponents;
	}
}
