package com.gcloud.header.security.msg.api.cluster;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiPageMessage;

public class ApiDescribeSecurityClusterAddableInstanceMsg extends ApiPageMessage {

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiDescribeSecurityClusterAddableInstanceReplyMsg.class;
	}
	
	@NotBlank
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
