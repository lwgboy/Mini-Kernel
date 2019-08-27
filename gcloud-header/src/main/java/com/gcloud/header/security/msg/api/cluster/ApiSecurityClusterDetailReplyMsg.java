package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.security.model.SecurityClusterDetailResponse;

public class ApiSecurityClusterDetailReplyMsg extends ApiReplyMessage{

	private static final long serialVersionUID = 1L;

	private SecurityClusterDetailResponse securityClusterDetail;

	public SecurityClusterDetailResponse getSecurityClusterDetail() {
		return securityClusterDetail;
	}

	public void setSecurityClusterDetail(SecurityClusterDetailResponse securityClusterDetail) {
		this.securityClusterDetail = securityClusterDetail;
	}
}
