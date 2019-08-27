package com.gcloud.core.workflow.enums;

public enum ParamType {
	RES("res"),
	REQ("req");
	
	String type;
	ParamType(String type)
	{
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
