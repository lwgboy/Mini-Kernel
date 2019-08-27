package com.gcloud.controller.image.enums;

public enum ImageStoreStatus {
	DOWNLOADING,
    DELETEING,
    ACTIVE;

    public String value() {
        return name().toLowerCase();
    }
}
