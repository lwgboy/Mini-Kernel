package com.gcloud.controller.network.model;

public class DescribeEipAddressesParams {
	private String eipAddress;
	private String allocationId;
	
	/**
	 * EIP的状态：
		Associating：绑定中
		Unassociating：解绑中
		InUse：已分配
		Available：可用
	 */
	private String status;

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
