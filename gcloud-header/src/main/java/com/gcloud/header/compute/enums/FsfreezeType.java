package com.gcloud.header.compute.enums;

public enum FsfreezeType {
	
	THAWED("thawed"), FROZEN("frozen");
	
	private String value;
	FsfreezeType(String name){
		this.value = name;
	}

	public String getValue() {
		return value;
	}

	public static FsfreezeType getByValue(String value) {
		for (FsfreezeType ft : FsfreezeType.values()) {
			if (ft.getValue().equals(value)) {
				return ft;
			}
		}
		return null;
	}
	
}
