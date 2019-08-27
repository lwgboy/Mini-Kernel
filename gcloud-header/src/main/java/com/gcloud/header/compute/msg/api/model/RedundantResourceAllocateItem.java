package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;

import com.gcloud.header.api.ApiModel;

public class RedundantResourceAllocateItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description = "实例类型")
    private String instanceType;
	@ApiModel(description = "配置")
    private String config;
	@ApiModel(description = "最大可分配")
	private int maxAllocate;
	@ApiModel(description = "实际可分配")
	private int actualAllocate;
	public String getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public int getMaxAllocate() {
		return maxAllocate;
	}
	public void setMaxAllocate(int maxAllocate) {
		this.maxAllocate = maxAllocate;
	}
	public int getActualAllocate() {
		return actualAllocate;
	}
	public void setActualAllocate(int actualAllocate) {
		this.actualAllocate = actualAllocate;
	}
	
}
