package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

public class DescribeEipAddressesMsg extends ApiPageMessage {
	@ApiModel(description="弹性公网IP地址")
	private String eipAddress;
	@ApiModel(description="弹性公网IP地址申请Id")
	private String allocationId;
	
	/**
	 * EIP的状态：
		Associating：绑定中
		Unassociating：解绑中
		InUse：已分配
		Available：可用
	 */
	@ApiModel(description="弹性公网IP地址状态")
	private String status;
	
	@Override
	public Class replyClazz() {
		return DescribeEipAddressesReplyMsg.class;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
