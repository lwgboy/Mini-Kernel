package com.gcloud.header.slb.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeLoadBalancerTCPListenerReplyMsg extends ApiReplyMessage {
	
	@ApiModel(description = "端口号")
	private Integer listenerPort;
	@ApiModel(description = "状态")
	private String  status;
	@ApiModel(description = "虚拟服务器组ID")
	private String vServerGroupId;
	
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
	
	

}
