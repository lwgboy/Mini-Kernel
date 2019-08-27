package com.gcloud.header.network.model;

import java.io.Serializable;

import com.gcloud.header.api.ApiModel;

public class NetworkInterfaceType implements Serializable{
	@ApiModel(description="网卡ID")
	private String networkInterfaceId;
	@ApiModel(description="网卡主私有 IP地址")
	private String primaryIpAddress;
	@ApiModel(description="网卡MAC地址")
	private String macAddress;
	public String getNetworkInterfaceId() {
		return networkInterfaceId;
	}
	public void setNetworkInterfaceId(String networkInterfaceId) {
		this.networkInterfaceId = networkInterfaceId;
	}
	public String getPrimaryIpAddress() {
		return primaryIpAddress;
	}
	public void setPrimaryIpAddress(String primaryIpAddress) {
		this.primaryIpAddress = primaryIpAddress;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
}
