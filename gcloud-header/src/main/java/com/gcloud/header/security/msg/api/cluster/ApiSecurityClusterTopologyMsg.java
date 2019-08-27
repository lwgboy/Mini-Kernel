package com.gcloud.header.security.msg.api.cluster;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;

public class ApiSecurityClusterTopologyMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiSecurityClusterTopologyReplyMsg.class;
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
