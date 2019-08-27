package com.gcloud.header.compute.enums;

public enum DiskFormatType {
	EXT4("ext4");

	private String value;

	DiskFormatType(String name) {
		this.value = name;
	}

	public String getValue() {
		return value;
	}
}
