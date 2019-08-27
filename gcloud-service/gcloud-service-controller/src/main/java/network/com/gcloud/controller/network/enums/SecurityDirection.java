package com.gcloud.controller.network.enums;

public enum SecurityDirection {
	INGRESS("ingress"), EGRESS("egress");
	
	private String value;
	SecurityDirection(String name){
		this.value = name;
	}

	public String getValue() {
		return value;
	}
}
