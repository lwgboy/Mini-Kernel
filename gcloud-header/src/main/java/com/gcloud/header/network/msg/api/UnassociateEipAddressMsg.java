package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class UnassociateEipAddressMsg extends ApiMessage {
	@NotBlank(message = "0050301::弹性公网实例Id不能为空")
	@ApiModel(description = "弹性公网实例Id", require = true)
	private String allocationId;
	@ApiModel(description = "实例Id")
	private String instanceId;
	
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

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
