package com.gcloud.header.compute.enums;

public enum ImageStoreType {
	LOCAL("local"),
	RBD("rbd"),
	LVM("Central"),
	SINGLE("single"),
	CACHE("cache");

	private String value;

	ImageStoreType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
