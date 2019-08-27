package com.gcloud.header.compute.enums;

public enum DeviceOwner {
	COMPUTE("compute:node"),
	FOREIGN("network:foreign"),
	DHCP("network:dhcp"),
	FLOATINGIP("network:floatingip"),
	ROUTER("network:router_interface"),
	LOADBALANCER("neutron:LOADBALANCERV2"),
	GATEWAY("network:router_gateway")
	;

	
	private String value;
	
	DeviceOwner (String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
