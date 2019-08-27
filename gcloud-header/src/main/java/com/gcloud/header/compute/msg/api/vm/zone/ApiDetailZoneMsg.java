package com.gcloud.header.compute.msg.api.vm.zone;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDetailZoneMsg extends ApiMessage{
	
	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiDetailZoneReplyMsg.class;
	}

	@ApiModel(description = "可用区ID", require = true)
	@NotBlank(message = "0180401::可用区ID不能为空")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
