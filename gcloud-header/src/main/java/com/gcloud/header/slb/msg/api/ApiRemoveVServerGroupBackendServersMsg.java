package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiRemoveVServerGroupBackendServersMsg extends ApiMessage {
	
	@ApiModel(description = "服务器组ID", require = true)
	@NotBlank(message = "0140701::后端服务器组ID不能为空")
	private String vServerGroupId;
	@ApiModel(description = "要删除的后端服务器列表", require = true)
	@NotBlank(message = "0140702::后端服务器信息不能为空")
	private String backendServers;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiRemoveVServerGroupBackendServersReplyMsg.class;
	}
	public String getvServerGroupId() {
		return vServerGroupId;
	}
	public void setvServerGroupId(String vServerGroupId) {
		this.vServerGroupId = vServerGroupId;
	}
	public String getBackendServers() {
		return backendServers;
	}
	public void setBackendServers(String backendServers) {
		this.backendServers = backendServers;
	}

}
