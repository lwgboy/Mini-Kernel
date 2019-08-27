package com.gcloud.controller.network.enums;

import java.util.Arrays;

public enum NetworkType {

	EXTERNAL("外部网络", 1),
	INTERNAL("内部网络", 0);
	
	private String name;
	private int value;
	
	NetworkType(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static NetworkType getNetworkTypeByValue(int value) {
		return Arrays.stream(NetworkType.values()).filter(t -> t.getValue() == value).findFirst().orElse(null);
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
	
	
}
