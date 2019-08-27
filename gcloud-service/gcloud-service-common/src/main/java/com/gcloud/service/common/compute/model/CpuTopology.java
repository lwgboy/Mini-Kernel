package com.gcloud.service.common.compute.model;

public class CpuTopology {
	
	private int socket;
	private int core;
	private int thread;
	
	public int getSocket() {
		return socket;
	}
	public void setSocket(int socket) {
		this.socket = socket;
	}
	public int getCore() {
		return core;
	}
	public void setCore(int core) {
		this.core = core;
	}
	public int getThread() {
		return thread;
	}
	public void setThread(int thread) {
		this.thread = thread;
	}
	
	
	
}
