package com.gcloud.controller.image.enums;

/**
 * Gcloud镜像后端存储类型
 *
 */
public enum ImageStorageType {
	FILE,
	RBD;
	
	public String value() {
        return name().toLowerCase();
    }
}
