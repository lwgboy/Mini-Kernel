package com.gcloud.header.security.model;

import java.io.Serializable;
import java.util.List;

public class ApiListSecurityClusterResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<SecurityClusterListType> securityClusterList;

	public List<SecurityClusterListType> getSecurityClusterList() {
		return securityClusterList;
	}

	public void setSecurityClusterList(List<SecurityClusterListType> securityClusterList) {
		this.securityClusterList = securityClusterList;
	}
}
