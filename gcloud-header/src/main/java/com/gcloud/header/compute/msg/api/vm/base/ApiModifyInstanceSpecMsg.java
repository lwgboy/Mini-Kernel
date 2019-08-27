package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.NotBlank;

public class ApiModifyInstanceSpecMsg extends ApiMessage {
	@ApiModel(description = "云服务器ID", require = true)
	@NotBlank(message = "0011301::云服务器ID不能为空")
	private String instanceId;
	@ApiModel(description = "实例类型ID", require = true)
	@NotBlank(message = "0011302::实例类型ID不能为空")
	private String instanceType;

	@Override
	public Class replyClazz() {
		return ApiModifyInstanceSpecReplyMsg.class;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
}
