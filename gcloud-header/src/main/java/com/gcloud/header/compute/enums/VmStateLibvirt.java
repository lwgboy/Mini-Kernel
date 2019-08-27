/*
 * @Date 2015-5-15
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
package com.gcloud.header.compute.enums;

public enum VmStateLibvirt {
	NOSTATE("Extant"), RUNNING("running"), BLOCKED("Extant"), PAUSED("paused"), SHUTDOWN(
			"Shutdown"), SHUTOFF("shut off"), CRASHED("StateException"), INSHUTDOWN("in shutdown");
	private String value;

	VmStateLibvirt(String name) {
		this.value = name;
	}

	public String getValue() {
		return value;
	}

}
