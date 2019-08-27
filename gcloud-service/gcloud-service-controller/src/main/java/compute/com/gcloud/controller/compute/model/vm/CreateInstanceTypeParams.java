package com.gcloud.controller.compute.model.vm;

public class CreateInstanceTypeParams {
	private String name;
	private Integer memory = 2048;
	private Integer cpu = 1;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
