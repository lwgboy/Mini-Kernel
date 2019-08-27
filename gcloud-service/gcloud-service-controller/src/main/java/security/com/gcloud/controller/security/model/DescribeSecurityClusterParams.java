package com.gcloud.controller.security.model;

import com.gcloud.common.model.PageParams;

public class DescribeSecurityClusterParams extends PageParams {
	
	private String securityClusterName;

	public String getSecurityClusterName() {
		return securityClusterName;
	}

	public void setSecurityClusterName(String securityClusterName) {
		this.securityClusterName = securityClusterName;
	}
}
