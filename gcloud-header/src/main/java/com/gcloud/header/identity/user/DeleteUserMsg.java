package com.gcloud.header.identity.user;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class DeleteUserMsg extends ApiMessage {
	@ApiModel(description = "用户ID", require = true)
	@NotBlank(message="2010301")
	private String id;
	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
