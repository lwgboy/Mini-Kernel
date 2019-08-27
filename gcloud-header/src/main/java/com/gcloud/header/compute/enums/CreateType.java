package com.gcloud.header.compute.enums;

public enum CreateType {
	
	IMAGE("image"),
	ISO("iso"),
	CLONE("clone");
	
	private String value;
	CreateType (String name){
		this.value = name;
	}

	public String getValue() {
		return value;
	}

	public static CreateType getByValue(String value) {
		for (CreateType ct : CreateType.values()) {
			if (ct.getValue().equals(value)) {
				return ct;
			}
		}
		return null;
	}
}
