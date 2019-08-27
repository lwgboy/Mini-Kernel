package com.gcloud.header.compute.enums;

public enum PlatformType {

	WINDOWS("windows"), LINUX("linux");
	
	private String value;
	
	PlatformType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
