package com.gcloud.header.slb.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeLoadBalancerHTTPSListenerReplyMsg extends ApiReplyMessage {
	
	@ApiModel(description = "端口号")
	private Integer listenerPort;
	@ApiModel(description = "状态")
	private String  status;
	@ApiModel(description = "虚拟服务器组ID")
	private String vServerGroupId;
	@ApiModel(description = "服务器证书ID")
	private String serverCertificateId;
	
	public Integer getListenerPort() {
		return listenerPort;
	}
	public void setListenerPort(Integer listenerPort) {
		this.listenerPort = listenerPort;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
	

}
