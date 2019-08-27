package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class AssociateEipAddressMsg extends ApiMessage {
	@NotBlank(message = "0050201::弹性公网实例Id不能为空")
	@ApiModel(description = "弹性公网 IP的申请 Id", require = true)
	private String allocationId;
	@NotBlank(message = "0050202::网卡Id不能为空")
	@ApiModel(description = "网卡Id", require = true)
	private String netcardId;//网卡Id
	@NotBlank(message = "0050203::云服务器Id不能为空")
	@ApiModel(description = "虚拟机Id", require = true)
	private String instanceId;//虚拟机Id
	
	
	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getNetcardId() {
		return netcardId;
	}

	public void setNetcardId(String netcardId) {
		this.netcardId = netcardId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

}
