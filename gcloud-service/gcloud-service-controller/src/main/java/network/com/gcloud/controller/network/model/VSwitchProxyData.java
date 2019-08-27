package com.gcloud.controller.network.model;

import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Subnet;

public class VSwitchProxyData {
	
	private Subnet subent;
	private Network network;
	private String networkId;
	private String subnetId;
	
	public Subnet getSubent() {
		return subent;
	}
	public void setSubent(Subnet subent) {
		this.subent = subent;
	}
	public Network getNetwork() {
		return network;
	}
	public void setNetwork(Network network) {
		this.network = network;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public String getSubnetId() {
		return subnetId;
	}
	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}
	
	
	

}
