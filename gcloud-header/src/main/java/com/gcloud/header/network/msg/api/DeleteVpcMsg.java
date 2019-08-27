package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiDeleteMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class DeleteVpcMsg extends ApiDeleteMessage {
	@ApiModel(description="专有网络ID", require=true)
	@NotBlank(message="0100101::专有网络ID不能为空")
	private String vpcId;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiReplyMessage.class;
	}
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

}
