package com.gcloud.header.security.model;

import java.io.Serializable;
import java.util.List;

public class DescribeSecurityClusterResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<SecurityClusterType> securityCluster;

	public List<SecurityClusterType> getSecurityCluster() {
		return securityCluster;
	}

	public void setSecurityCluster(List<SecurityClusterType> securityCluster) {
		this.securityCluster = securityCluster;
	}
}
