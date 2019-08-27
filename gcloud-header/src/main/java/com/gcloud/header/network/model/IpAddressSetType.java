package com.gcloud.header.network.model;

import com.gcloud.header.api.ApiModel;

import java.io.Serializable;
import java.util.List;

public class IpAddressSetType implements Serializable{
	@ApiModel(description="IP地址")
	private List<String> ipAddress;

	public List<String> getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(List<String> ipAddress) {
		this.ipAddress = ipAddress;
	}
}
