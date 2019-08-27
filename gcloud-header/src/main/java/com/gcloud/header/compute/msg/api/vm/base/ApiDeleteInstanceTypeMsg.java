package com.gcloud.header.compute.msg.api.vm.base;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDeleteInstanceTypeMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@ApiModel(description = "实例类型ID", require = true)
	@NotBlank(message = "0011501::实例类型ID不能为空")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
