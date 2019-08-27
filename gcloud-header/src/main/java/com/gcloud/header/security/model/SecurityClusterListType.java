package com.gcloud.header.security.model;

import java.io.Serializable;
import java.util.List;

public class SecurityClusterListType implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String protectionNetCidr;
	private String state;
	private String stateCnName;
	private String createUser;
	private String createUserName;
	private String createTime;
	private List<SecurityClusterComponentType> securityClusterComponents;
	
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
	public List<SecurityClusterComponentType> getSecurityClusterComponents() {
		return securityClusterComponents;
	}
	public void setSecurityClusterComponents(List<SecurityClusterComponentType> securityClusterComponents) {
		this.securityClusterComponents = securityClusterComponents;
	}
}
