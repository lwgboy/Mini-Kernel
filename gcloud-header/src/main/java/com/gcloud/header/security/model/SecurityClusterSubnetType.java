package com.gcloud.header.security.model;

import java.io.Serializable;
import java.util.Date;

public class SecurityClusterSubnetType implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String clusterId;
	private String subnetId;
	private String networkId;
	private Date createTime;
	private String createUser;
	private Date updateTime;
	private String networkType;
	private String cidr;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getSubnetId() {
		return subnetId;
	}
	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getCidr() {
		return cidr;
	}
	public void setCidr(String cidr) {
		this.cidr = cidr;
	}
}
