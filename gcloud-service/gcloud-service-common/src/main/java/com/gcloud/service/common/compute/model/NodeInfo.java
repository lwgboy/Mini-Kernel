package com.gcloud.service.common.compute.model;

public class NodeInfo {

	private String cpuModel;  // eg：x86_64
	private int cpus; // 个
	private int cpuFrequency; // 单位MHz
	private int cpuSockets;
	private int CoresPerSocket;
	private int ThreadsPerCore;
	private int numaCells;
	private int memorys; // KiB
	public String getCpuModel() {
		return cpuModel;
	}
	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}
	public int getCpus() {
		return cpus;
	}
	public void setCpus(int cpus) {
		this.cpus = cpus;
	}
	public int getCpuFrequency() {
		return cpuFrequency;
	}
	public void setCpuFrequency(int cpuFrequency) {
		this.cpuFrequency = cpuFrequency;
	}
	public int getCpuSockets() {
		return cpuSockets;
	}
	public void setCpuSockets(int cpuSockets) {
		this.cpuSockets = cpuSockets;
	}
	public int getCoresPerSocket() {
		return CoresPerSocket;
	}
	public void setCoresPerSocket(int coresPerSocket) {
		CoresPerSocket = coresPerSocket;
	}
	public int getThreadsPerCore() {
		return ThreadsPerCore;
	}
	public void setThreadsPerCore(int threadsPerCore) {
		ThreadsPerCore = threadsPerCore;
	}
	public int getNumaCells() {
		return numaCells;
	}
	public void setNumaCells(int numaCells) {
		this.numaCells = numaCells;
	}
	public int getMemorys() {
		return memorys;
	}
	public void setMemorys(int memorys) {
		this.memorys = memorys;
	}
}
