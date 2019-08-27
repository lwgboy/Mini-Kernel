package com.gcloud.header.slb.model;

import java.io.Serializable;

import com.gcloud.header.api.ApiModel;

public class BackendServerSetType implements Serializable{
	
	@ApiModel(description = "实例ID")
	private String serverId;
	@ApiModel(description = "端口号")
	private Integer port;
	@ApiModel(description = "权重")
	private Integer weight;
	@ApiModel(description = "后端服务器类型")
	private String type;
	@ApiModel(description = "后端服务器标识")
	private String memberId;
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
