package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiSetLoadBalancerTCPListenerMsg extends ApiMessage {

	@ApiModel(description = "监听器ID", require = true)
	@NotBlank(message = "0120601::监听器ID不能为空")
	private String  listenerId;
	@ApiModel(description = "虚拟服务器组ID", require = true)
	@NotBlank(message = "0120602::虚拟服务器组ID不能为空")
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
		return ApiSetLoadBalancerTCPListenerReplyMsg.class;
	}

}
