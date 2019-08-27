package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class DetachVSwitchVRouterMsg extends ApiMessage {
	@NotBlank(message="0020801")
	@ApiModel(description = "路由器Id", require = true)
	private String vRouterId;
	@NotBlank(message="0020802")
	@ApiModel(description = "交换机Id", require = true)
	private String vSwitchId;
	
	public String getvRouterId() {
		return vRouterId;
	}

	public void setvRouterId(String vRouterId) {
		this.vRouterId = vRouterId;
	}

	public String getvSwitchId() {
		return vSwitchId;
	}

	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}
	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

}
