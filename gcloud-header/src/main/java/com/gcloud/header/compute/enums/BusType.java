package com.gcloud.header.compute.enums;

public enum BusType {
	
	VIRTIO("virtio");
	
	private String value;
	BusType(String name){
		this.value = name;
	}

	public String getValue() {
		return value;
	}

}
