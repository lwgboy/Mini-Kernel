package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ReleaseEipAddressMsg extends ApiMessage {
	
	@ApiModel(description = "弹性公网实例Id", require = true)
	@NotBlank(message = "0050401::弹性公网实例Id不能为空")
	private String allocationId;
	
	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

}
