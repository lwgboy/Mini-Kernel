package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ModifyEipAddressAttributeMsg extends ApiMessage {
	@ApiModel(description = "弹性 IP地址申请Id", require = true)
	@NotBlank(message = "0050501::弹性 IP地址申请ID不能为空")
	private String allocationId;
	@ApiModel(description = "弹性 IP地址带宽", require = true)
	@NotBlank(message = "0050502::弹性 IP地址带宽不能为空")
	private Integer bandwidth;//带宽以 Mbps 计算
	
	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public Integer getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
}
