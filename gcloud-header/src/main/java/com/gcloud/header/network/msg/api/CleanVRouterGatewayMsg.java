package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CleanVRouterGatewayMsg extends ApiMessage {
	@NotBlank(message = "0020601::路由ID不能为空")
	@ApiModel(description="路由ID")
	private String vRouterId;
	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	public String getvRouterId() {
		return vRouterId;
	}
	public void setvRouterId(String vRouterId) {
		this.vRouterId = vRouterId;
	}

}
