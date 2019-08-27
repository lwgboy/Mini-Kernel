package com.gcloud.header.security.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.security.model.SecurityClusterComponentType;
import com.gcloud.header.security.model.SecurityClusterInstanceType;
import com.gcloud.header.security.model.SecurityClusterSubnetType;

public class SecurityClusterTopologyResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String clusterId;
	private List<SecurityClusterComponentType> components;
	private List<SecurityClusterInstanceType> instances;
	private SecurityClusterSubnetType subnet;
	private boolean ha;
	
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public List<SecurityClusterComponentType> getComponents() {
		return components;
	}
	public void setComponents(List<SecurityClusterComponentType> components) {
		this.components = components;
	}
	public List<SecurityClusterInstanceType> getInstances() {
		return instances;
	}
	public void setInstances(List<SecurityClusterInstanceType> instances) {
		this.instances = instances;
	}
	public SecurityClusterSubnetType getSubnet() {
		return subnet;
	}
	public void setSubnet(SecurityClusterSubnetType subnet) {
		this.subnet = subnet;
	}
	public boolean isHa() {
		return ha;
	}
	public void setHa(boolean ha) {
		this.ha = ha;
	}
}
