package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeVServerGroupAttributeMsg extends ApiMessage {
	
	@ApiModel(description = "虚拟服务器组ID", require = true)
	@NotBlank(message = "0140401::后端服务器组ID不能为空")
	private String vServerGroupId;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiDescribeVServerGroupAttributeReplyMsg.class;
	}
	public String getvServerGroupId() {
		return vServerGroupId;
	}
	public void setvServerGroupId(String vServerGroupId) {
		this.vServerGroupId = vServerGroupId;
	}

}
