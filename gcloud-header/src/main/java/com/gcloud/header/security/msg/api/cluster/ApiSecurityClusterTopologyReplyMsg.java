package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.security.model.SecurityClusterTopologyResponse;

public class ApiSecurityClusterTopologyReplyMsg extends ApiReplyMessage{

	private static final long serialVersionUID = 1L;

	private SecurityClusterTopologyResponse securityClusterTopology;

	public SecurityClusterTopologyResponse getSecurityClusterTopology() {
		return securityClusterTopology;
	}

	public void setSecurityClusterTopology(SecurityClusterTopologyResponse securityClusterTopology) {
		this.securityClusterTopology = securityClusterTopology;
	}
}
