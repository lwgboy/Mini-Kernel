package com.gcloud.header.slb.model;

import java.io.Serializable;

import com.gcloud.header.api.ApiModel;

public class VServerGroupSetType implements Serializable{
	
	@ApiModel(description = "服务器组ID")
	private String vServerGroupId;
	@ApiModel(description = "服务器组名称")
	private String vServerGroupName;
	@ApiModel(description = "服务器组协议，取值范围HTTP、TCP")
	private String vServerGroupProtocol;
	
	public String getvServerGroupId() {
		return vServerGroupId;
	}
	public void setvServerGroupId(String vServerGroupId) {
		this.vServerGroupId = vServerGroupId;
	}
	public String getvServerGroupName() {
		return vServerGroupName;
	}
	public void setvServerGroupName(String vServerGroupName) {
		this.vServerGroupName = vServerGroupName;
	}
	public String getvServerGroupProtocol() {
		return vServerGroupProtocol;
	}
	public void setvServerGroupProtocol(String vServerGroupProtocol) {
		this.vServerGroupProtocol = vServerGroupProtocol;
	}
}
