package com.gcloud.header.slb.msg.api;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiCreateLoadBalancerHTTPSListenerMsg extends ApiMessage {

	@ApiModel(description = "负载均衡ID", require = true)
	@NotBlank(message = "0120201::负载均衡ID不能为空")
	private String  loadBalancerId;
	@ApiModel(description = "端口号", require = true)
	@NotNull(message = "0120202::端口号不能为空")
	@Max(value = 65535, message = "0120203::端口号不能超过65535")
	@Min(value = 1, message = "0120204::端口号不能低于1")
	private int   listenerPort;
	@ApiModel(description = "虚拟服务器组ID", require = true)
	@NotBlank(message = "0120205::虚拟服务器组ID不能为空")
	private String   vServerGroupId;
	@ApiModel(description = "服务器证书ID", require = true)
	@NotBlank(message = "0120206::服务器证书ID不能为空")
	private String   serverCertificateId;
	
	public String getLoadBalancerId() {
		return loadBalancerId;
	}

	public void setLoadBalancerId(String loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}

	public int getListenerPort() {
		return listenerPort;
	}

	public void setListenerPort(int listenerPort) {
		this.listenerPort = listenerPort;
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
		return ApiCreateLoadBalancerHTTPSListenerReplyMsg.class;
	}

}
