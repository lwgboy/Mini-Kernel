package com.gcloud.header.slb.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.slb.model.BackendServerResponse;

public class ApiDescribeVServerGroupAttributeReplyMsg extends ApiReplyMessage{
	
	@ApiModel(description = "服务器组ID")
	private String VServerGroupId;
	@ApiModel(description = "服务器组名称")
	private String VServerGroupName;
	@ApiModel(description = "后端服务器列表")
	private BackendServerResponse backendServers;
	
	public String getVServerGroupId() {
		return VServerGroupId;
	}
	public void setVServerGroupId(String vServerGroupId) {
		VServerGroupId = vServerGroupId;
	}
	public String getVServerGroupName() {
		return VServerGroupName;
	}
	public void setVServerGroupName(String vServerGroupName) {
		VServerGroupName = vServerGroupName;
	}
	public BackendServerResponse getBackendServers() {
		return backendServers;
	}
	public void setBackendServers(BackendServerResponse backendServers) {
		this.backendServers = backendServers;
	}
}
