package com.gcloud.header.compute.msg.node.node.model;

import java.io.Serializable;

public class NodeBaseInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String ip;
	private String hostName;
	private String type;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
