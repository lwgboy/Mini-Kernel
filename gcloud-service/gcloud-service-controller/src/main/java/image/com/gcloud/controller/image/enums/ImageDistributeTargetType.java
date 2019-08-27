package com.gcloud.controller.image.enums;

public enum ImageDistributeTargetType {
	NODE,
	RBD,
	VG;
	
	public String value() {
        return name().toLowerCase();
    }
}
