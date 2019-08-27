package com.gcloud.header.security.msg.api.cluster;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;

public class ApiSecurityClusterRemoveInstanceMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@NotBlank
	private String id;
	@NotBlank
	private String instanceId;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
