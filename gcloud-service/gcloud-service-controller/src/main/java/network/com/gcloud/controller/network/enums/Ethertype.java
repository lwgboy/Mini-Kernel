package com.gcloud.controller.network.enums;

public enum Ethertype {
	IPv4("IPv4"), IPv6("IPv6");
	
	private String value;
	
	Ethertype(String name){
		this.value = name;
	}

	public String getValue() {
		return value;
	}
}
