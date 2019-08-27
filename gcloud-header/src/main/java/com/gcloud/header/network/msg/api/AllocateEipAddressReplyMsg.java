package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class AllocateEipAddressReplyMsg extends ApiReplyMessage {
	@ApiModel(description = "弹性公网 IP")
	private String eipAddress;
	@ApiModel(description = "弹性公网 IP的申请 Id")
	private String allocationId;
	
	public String getEipAddress() {
		return eipAddress;
	}
	public void setEipAddress(String eipAddress) {
		this.eipAddress = eipAddress;
	}
	public String getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}
	
}