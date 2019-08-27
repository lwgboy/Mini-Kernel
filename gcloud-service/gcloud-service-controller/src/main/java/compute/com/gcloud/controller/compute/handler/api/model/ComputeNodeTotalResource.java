package com.gcloud.controller.compute.handler.api.model;

public class ComputeNodeTotalResource {
	private String hostname;
	private int totalCore;
	private int totalMemory;
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getTotalCore() {
		return totalCore;
	}
	public void setTotalCore(int totalCore) {
		this.totalCore = totalCore;
	}
	public int getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(int totalMemory) {
		this.totalMemory = totalMemory;
	}
	
}
