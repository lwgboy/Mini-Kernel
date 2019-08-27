package com.gcloud.header.compute.msg.api.vm.base;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiAssociateInstanceTypeMsg extends ApiMessage {

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@ApiModel(description = "实例类型ID", require = true)
	@NotBlank(message = "0011801::实例类型ID不能为空")
	private String instanceTypeId;
	
	@ApiModel(description ="可用区ID", require = true)
	@NotBlank(message = "0011802::可用区ID不能为空")
	private String zoneId;

	public String getInstanceTypeId() {
		return instanceTypeId;
	}
	public void setInstanceTypeId(String instanceTypeId) {
		this.instanceTypeId = instanceTypeId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
