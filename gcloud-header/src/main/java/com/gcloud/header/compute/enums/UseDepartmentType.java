package com.gcloud.header.compute.enums;

public enum UseDepartmentType {
	GCLOUD("GCLOUD");
	private String value;

	UseDepartmentType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
