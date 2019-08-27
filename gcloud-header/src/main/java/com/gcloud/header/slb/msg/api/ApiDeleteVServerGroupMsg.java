package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDeleteVServerGroupMsg extends ApiMessage {
	
	@ApiModel(description = "虚拟服务器组ID", require = true)
	@NotBlank(message = "0140501::后端服务器组ID不能为空")
	private String vServerGroupId;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiDeleteVServerGroupReplyMsg.class;
	}
	public String getvServerGroupId() {
		return vServerGroupId;
	}
	public void setvServerGroupId(String vServerGroupId) {
		this.vServerGroupId = vServerGroupId;
	}

}
