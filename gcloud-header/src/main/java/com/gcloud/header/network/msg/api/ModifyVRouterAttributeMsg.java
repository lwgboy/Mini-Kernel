package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ModifyVRouterAttributeMsg  extends ApiMessage{
	@NotBlank(message = "0020201")
	@ApiModel(description="路由Id", require=true)
	private String vRouterId;
	@NotBlank(message = "0020202")
	@ApiModel(description="路由名称", require=true)
	private String vRouterName;
	
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

	public String getvRouterName() {
		return vRouterName;
	}

	public void setvRouterName(String vRouterName) {
		this.vRouterName = vRouterName;
	}
}
