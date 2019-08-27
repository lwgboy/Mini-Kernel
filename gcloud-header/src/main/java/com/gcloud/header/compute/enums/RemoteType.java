package com.gcloud.header.compute.enums;

public enum RemoteType {

	VNC("vnc"),
	SPICE("spice"),
	RDP("rdp");
	
	private String value;
	RemoteType(String value){
		this.value = value;
	}
	public String getValue(){
		return value;
	}
	
	
}
