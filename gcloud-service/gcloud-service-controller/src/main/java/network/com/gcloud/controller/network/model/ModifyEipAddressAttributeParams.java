package com.gcloud.controller.network.model;

public class ModifyEipAddressAttributeParams {
	private String allocationId;
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
}
