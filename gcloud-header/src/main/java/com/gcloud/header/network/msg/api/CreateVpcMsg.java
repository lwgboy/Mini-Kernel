package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiCreateMessage;
import com.gcloud.header.api.ApiModel;

public class CreateVpcMsg extends ApiCreateMessage {
	
	@ApiModel(description="专有网络ID", require=true)
	@NotBlank(message = "0100101::专有网络名称不能为空")
	private String vpcName;
	
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return CreateVpcReplyMsg.class;
	}
	public String getVpcName() {
		return vpcName;
	}
	public void setVpcName(String vpcName) {
		this.vpcName = vpcName;
	}
	

}
