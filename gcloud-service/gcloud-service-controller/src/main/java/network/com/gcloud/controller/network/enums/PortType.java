package com.gcloud.controller.network.enums;

import java.util.Arrays;

public enum PortType {

	COMPUTE("compute:node"),FOREIGN("network:foreign");

	private String value;

	PortType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static PortType getByValue(String value){
		return Arrays.stream(PortType.values()).filter(t -> t.getValue().equals(value)).findFirst().orElse(null);
	}
}
