package com.gcloud.core.workflow.enums;

public enum FromRelationType {
	FROM_ONE_DONE("FROM_ONE_DONE"),
	FROM_ALL_DONE("FROM_ALL_DONE");
	
	String type;
	FromRelationType(String type)
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
