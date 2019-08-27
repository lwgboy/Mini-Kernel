package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ModifyVSwitchAttributeMsg extends ApiMessage {
	@NotBlank(message = "0030201::交换机ID不能为空")
	@ApiModel(description = "交换机Id", require = true)
	private String vSwitchId;
	@ApiModel(description = "交换机名称", require = true)
	@NotBlank(message = "0030202::交换机名称不能为空")
	private String vSwitchName;
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
	public String getvSwitchName() {
		return vSwitchName;
	}
	public void setvSwitchName(String vSwitchName) {
		this.vSwitchName = vSwitchName;
	}
}
