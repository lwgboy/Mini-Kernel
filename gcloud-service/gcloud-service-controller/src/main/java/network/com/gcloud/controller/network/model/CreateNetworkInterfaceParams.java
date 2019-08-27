package com.gcloud.controller.network.model;

public class CreateNetworkInterfaceParams {
	private String vSwitchId;
	private String primaryIpAddress;
	private String securityGroupId;
	private String networkInterfaceName;
	private String description;
	
	public String getvSwitchId() {
		return vSwitchId;
	}
	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}
	public String getPrimaryIpAddress() {
		return primaryIpAddress;
	}
	public void setPrimaryIpAddress(String primaryIpAddress) {
		this.primaryIpAddress = primaryIpAddress;
	}
	public String getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	public String getNetworkInterfaceName() {
		return networkInterfaceName;
	}
	public void setNetworkInterfaceName(String networkInterfaceName) {
		this.networkInterfaceName = networkInterfaceName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
