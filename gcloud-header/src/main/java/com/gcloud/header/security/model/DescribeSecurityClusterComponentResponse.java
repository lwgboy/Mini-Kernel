package com.gcloud.header.security.model;

import java.io.Serializable;
import java.util.List;

public class DescribeSecurityClusterComponentResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<SecurityClusterComponentType> securityClusterComponent;

	public List<SecurityClusterComponentType> getSecurityClusterComponent() {
		return securityClusterComponent;
	}

	public void setSecurityClusterComponent(List<SecurityClusterComponentType> securityClusterComponent) {
		this.securityClusterComponent = securityClusterComponent;
	}
}
