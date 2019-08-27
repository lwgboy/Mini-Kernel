package com.gcloud.header.slb.msg.api;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiSetLoadBalancerHTTPListenerMsg extends ApiMessage {

	@ApiModel(description = "监听器ID", require = true)
	@NotBlank(message = "0120401::监听器ID不能为空")
	private String  listenerId;
	@ApiModel(description = "虚拟服务器组ID", require = true)
	@NotBlank(message = "0120402::虚拟服务器组ID不能为空")
	private String   vServerGroupId;

	public String getListenerId() {
		return listenerId;
	}



	public void setListenerId(String listenerId) {
		this.listenerId = listenerId;
	}



	public String getvServerGroupId() {
		return vServerGroupId;
	}



	public void setvServerGroupId(String vServerGroupId) {
		this.vServerGroupId = vServerGroupId;
	}



	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiSetLoadBalancerHTTPListenerReplyMsg.class;
	}

}
