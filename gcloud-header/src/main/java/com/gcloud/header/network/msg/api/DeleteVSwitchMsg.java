package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class DeleteVSwitchMsg  extends ApiMessage{
	@NotBlank(message = "0030401::交换机ID不能为空")
	@ApiModel(description = "交换机Id", require = true)
	private String vSwitchId;
	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	public String getvSwitchId() {
		return vSwitchId;
	}
	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}
	
}
