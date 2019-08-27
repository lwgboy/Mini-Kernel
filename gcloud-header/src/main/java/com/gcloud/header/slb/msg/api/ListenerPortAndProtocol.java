package com.gcloud.header.slb.msg.api;

import java.io.Serializable;

import com.gcloud.header.api.ApiModel;

public class ListenerPortAndProtocol implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModel(description = "监听器ID")
	private String listenerId;
	@ApiModel(description = "端口号")
	private Integer listenerPort;
	@ApiModel(description = "协议")
	private String listenerProtocol;
	
	public String getListenerId() {
		return listenerId;
	}
	public void setListenerId(String listenerId) {
		this.listenerId = listenerId;
	}
	public Integer getListenerPort() {
		return listenerPort;
	}
	public void setListenerPort(Integer listenerPort) {
		this.listenerPort = listenerPort;
	}
	public String getListenerProtocol() {
		return listenerProtocol;
	}
	public void setListenerProtocol(String listenerProtocol) {
		this.listenerProtocol = listenerProtocol;
	}
	
	
}
