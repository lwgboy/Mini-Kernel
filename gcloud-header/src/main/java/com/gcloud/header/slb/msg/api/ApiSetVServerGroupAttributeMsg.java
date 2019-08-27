package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiSetVServerGroupAttributeMsg extends ApiMessage {
	
	@ApiModel(description = "服务器组ID", require = true)
	@NotBlank(message = "0140201::后端服务器组ID不能为空")
	private String vServerGroupId;
	@ApiModel(description = "服务器组名称", require = true)
	@NotBlank(message = "0140202::后端服务器组名称不能为空")
	private String vServerGroupName;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiSetVServerGroupAttributeReplyMsg.class;
	}
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

}
