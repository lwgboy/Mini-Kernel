package com.gcloud.controller.network.model;

import java.util.ArrayList;
import java.util.List;

public class ModifyNetworkInterfaceAttributeParams {
	private String networkInterfaceId;
	private List<String> securityGroupId = new ArrayList<String>();
	private String networkInterfaceName;
	private String description;
	
	public String getNetworkInterfaceId() {
		return networkInterfaceId;
	}
	public void setNetworkInterfaceId(String networkInterfaceId) {
		this.networkInterfaceId = networkInterfaceId;
	}
	public List<String> getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(List<String> securityGroupId) {
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
