package com.gcloud.header.security.msg.api.cluster;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.security.model.DescribeSecurityClusterComponentResponse;
import com.gcloud.header.security.model.SecurityClusterComponentType;

public class ApiDescribeSecurityClusterComponentReplyMsg extends PageReplyMessage<SecurityClusterComponentType>{

	private static final long serialVersionUID = 1L;

	private DescribeSecurityClusterComponentResponse securityClusterComponents;
	
	@Override
	public void setList(List<SecurityClusterComponentType> list) {
		securityClusterComponents = new DescribeSecurityClusterComponentResponse();
		securityClusterComponents.setSecurityClusterComponent(list);
	}

	public DescribeSecurityClusterComponentResponse getSecurityClusterComponents() {
		return securityClusterComponents;
	}

	public void setSecurityClusterComponents(DescribeSecurityClusterComponentResponse securityClusterComponents) {
		this.securityClusterComponents = securityClusterComponents;
	}
}
