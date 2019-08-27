package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

public class SecurityGroupIdSetType  implements Serializable{
	private List<String> securityGroupId;

	public List<String> getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(List<String> securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
}
