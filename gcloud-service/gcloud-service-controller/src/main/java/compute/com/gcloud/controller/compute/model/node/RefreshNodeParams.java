package com.gcloud.controller.compute.model.node;

public class RefreshNodeParams {
	private float cpuUtil = 100f;
	private float memoryUtil = 100f;

	public float getCpuUtil() {
		return cpuUtil;
	}

	public void setCpuUtil(float cpuUtil) {
		this.cpuUtil = cpuUtil;
	}

	public float getMemoryUtil() {
		return memoryUtil;
	}

	public void setMemoryUtil(float memoryUtil) {
		this.memoryUtil = memoryUtil;
	}
}
