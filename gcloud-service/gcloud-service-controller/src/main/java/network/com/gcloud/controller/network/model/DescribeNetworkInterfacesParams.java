package com.gcloud.controller.network.model;

import com.gcloud.common.model.PageParams;

import java.util.List;

public class DescribeNetworkInterfacesParams extends PageParams {
	private String vSwitchId;
	private String primaryIpAddress;
	private String securityGroupId;
	private String networkInterfaceName;
	private String instanceId;
	private List<String> networkInterfaceIds;
	private List<String> deviceOwners;
	private Boolean includeOwnerless;


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

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public List<String> getNetworkInterfaceIds() {
		return networkInterfaceIds;
	}

	public void setNetworkInterfaceIds(List<String> networkInterfaceIds) {
		this.networkInterfaceIds = networkInterfaceIds;
	}

	public List<String> getDeviceOwners() {
		return deviceOwners;
	}

	public void setDeviceOwners(List<String> deviceOwners) {
		this.deviceOwners = deviceOwners;
	}

	public Boolean getIncludeOwnerless() {
		return includeOwnerless;
	}

	public void setIncludeOwnerless(Boolean includeOwnerless) {
		this.includeOwnerless = includeOwnerless;
	}
}
