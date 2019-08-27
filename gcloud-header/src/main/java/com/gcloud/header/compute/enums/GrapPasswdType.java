package com.gcloud.header.compute.enums;

public enum GrapPasswdType {
	RONDOM("RD");
	private String passwd;
	GrapPasswdType(String passwd){
		this.passwd = passwd;
	}
	public String getPasswd() {
		return passwd;
	}	
}
