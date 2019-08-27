package com.gcloud.controller.network.model;

public class AssociateEipAddressParams {
	private String allocationId;
	private String netcardId;//网卡Id不支持阿里的实现
	private String instanceId;//虚拟机Id仅支持阿里的实现
	
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
	
}
