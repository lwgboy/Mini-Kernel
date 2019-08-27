package com.gcloud.controller.compute.model.vm;

public class ModifyInstanceTypeParams {
	private String id;
	private Integer memory;
	private Integer cpu;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public Integer getCpu() {
		return cpu;
	}
	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}
}
