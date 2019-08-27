package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class CreateVRouterMsg extends ApiMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description="路由器名称",require=true)
	@NotBlank(message="0020101")
	private String vRouterName;

	public String getvRouterName() {
		return vRouterName;
	}

	public void setvRouterName(String vRouterName) {
		this.vRouterName = vRouterName;
	}
	@Override
	public Class replyClazz() {
		return CreateVRouterReplyMsg.class;
	}

}
