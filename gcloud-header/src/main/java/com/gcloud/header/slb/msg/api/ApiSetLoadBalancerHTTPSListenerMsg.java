package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiSetLoadBalancerHTTPSListenerMsg extends ApiMessage {

	@ApiModel(description = "监听器ID", require = true)
	@NotBlank(message = "0120501::监听器ID不能为空")
	private String  listenerId;
	@ApiModel(description = "虚拟服务器组ID", require = true)
	@NotBlank(message = "0120502::虚拟服务器组ID不能为空")
	private String   vServerGroupId;
	@ApiModel(description = "服务器证书ID", require = true)
	@NotBlank(message = "0120503::服务器证书ID不能为空")
	private String   serverCertificateId;

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



	public String getServerCertificateId() {
		return serverCertificateId;
	}



	public void setServerCertificateId(String serverCertificateId) {
		this.serverCertificateId = serverCertificateId;
	}



	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiSetLoadBalancerHTTPSListenerReplyMsg.class;
	}
}
