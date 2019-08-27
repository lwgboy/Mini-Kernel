package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ModifyVpcAttributeMsg extends ApiMessage{
	@ApiModel(description="专有网络ID", require=true)
	@NotBlank(message="0100201")
	private String vpcId;
	@ApiModel(description="专有网络名称", require=true)
	@Length(min=2, max = 20, message = "0100402::专有网络名称不能为空")
	private String vpcName;
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
	public String getVpcName() {
		return vpcName;
	}
	public void setVpcName(String vpcName) {
		this.vpcName = vpcName;
	}

}
