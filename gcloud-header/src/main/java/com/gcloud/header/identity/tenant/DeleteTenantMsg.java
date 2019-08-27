package com.gcloud.header.identity.tenant;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class DeleteTenantMsg extends ApiMessage{
	@ApiModel(description = "租户ID", require = true)
	@NotBlank(message = "2010301")
	private String id;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
}
