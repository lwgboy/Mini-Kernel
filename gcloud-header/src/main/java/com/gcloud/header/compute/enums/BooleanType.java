package com.gcloud.header.compute.enums;

public enum BooleanType {
	FALSE(0, "false"), TRUE(1, "true");

	private Integer value;
	private String name;

	BooleanType(Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}
