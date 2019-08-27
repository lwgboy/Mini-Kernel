package com.gcloud.header.compute.msg.api.vm.zone;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDeleteZoneMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@ApiModel(description = "可用区ID", require = true)
	@NotBlank(message = "0180201::可用区ID不能为空")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
