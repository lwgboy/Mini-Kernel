package com.gcloud.controller.compute.handler.api.model;

public class ComputeNodeUsedResource {
	private String hostname;
	private int usedCore;
	private int usedMemory;
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getUsedCore() {
		return usedCore;
	}
	public void setUsedCore(int usedCore) {
		this.usedCore = usedCore;
	}
	public int getUsedMemory() {
		return usedMemory;
	}
	public void setUsedMemory(int usedMemory) {
		this.usedMemory = usedMemory;
	}
	
}
