package com.gcloud.header.compute.enums;

public enum TrashState {

	TRASHED("trashed"),
	LOGICALDELETEING("logicalDeleting"),
	RESTORING("restoring"),
	PHYSICALDELETING("physicalDeleting"),
	PHYSICALFAILED("physicalFailed");
	
	private String value;
	
	TrashState(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
}
