package com.gcloud.header.security.model;

import java.io.Serializable;
import java.util.List;

public class DescribeSecurityClusterAddableInstanceResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<SecurityClusterInstanceType> securityClusterAddableInstance;

	public List<SecurityClusterInstanceType> getSecurityClusterAddableInstance() {
		return securityClusterAddableInstance;
	}

	public void setSecurityClusterAddableInstance(List<SecurityClusterInstanceType> securityClusterAddableInstance) {
		this.securityClusterAddableInstance = securityClusterAddableInstance;
	}
}
