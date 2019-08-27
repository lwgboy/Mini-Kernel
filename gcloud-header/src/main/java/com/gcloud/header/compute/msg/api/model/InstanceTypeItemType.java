package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;

import com.gcloud.header.api.ApiModel;

public class InstanceTypeItemType implements Serializable{
	@ApiModel(description="实例规格的ID")
	private String instanceTypeId;
	@ApiModel(description="实例规格的ID")
	private String instanceTypeName;
	@ApiModel(description="CPU的内核数目")
	private Integer cpuCoreCount;
	@ApiModel(description="内存大小，单位MB")
	private Double memorySize; // 单位MB
	
	public String getInstanceTypeId() {
		return instanceTypeId;
	}
	public void setInstanceTypeId(String instanceTypeId) {
		this.instanceTypeId = instanceTypeId;
	}
	public String getInstanceTypeName() {
		return instanceTypeName;
	}
	public void setInstanceTypeName(String instanceTypeName) {
		this.instanceTypeName = instanceTypeName;
	}
	public Integer getCpuCoreCount() {
		return cpuCoreCount;
	}
	public void setCpuCoreCount(Integer cpuCoreCount) {
		this.cpuCoreCount = cpuCoreCount;
	}
	public Double getMemorySize() {
		return memorySize;
	}
	public void setMemorySize(Double memorySize) {
		this.memorySize = memorySize;
	}
}
