package com.gcloud.header.compute.enums;

public enum AgentType {
	
	VM_AGENT("vmagent"), QEMU_GUEST_AGENT("qemu-guest-agent");
	
	private String value;
	AgentType(String name){
		this.value = name;
	}

	public String getValue() {
		return value;
	}

}
