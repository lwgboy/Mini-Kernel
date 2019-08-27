package com.gcloud.controller.compute.model.node;

/*
 * @Date 2015-4-14
 * 
 * @Author zhangzj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
public class ResourceUnit {
	private int core;
	private int memory;

	public int getCore() {
		return core;
	}

	public int getMemory() {
		return memory;
	}

	public void addCore(int core) {
		this.core += core;
	}

	public void addMemory(int memory) {
		this.memory += memory;
	}

	public void setCore(int core) {
		this.core = core;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}
}
