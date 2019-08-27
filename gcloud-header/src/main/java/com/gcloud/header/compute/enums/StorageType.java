package com.gcloud.header.compute.enums;

import com.gcloud.header.storage.enums.StoragePoolDriver;

import java.util.Arrays;

public enum StorageType {
    LOCAL(StoragePoolDriver.FILE),
    DISTRIBUTED(StoragePoolDriver.RBD),
    CENTRAL(StoragePoolDriver.LVM);

    private StoragePoolDriver defaultDriver;

    StorageType(StoragePoolDriver defaultDriver) {
        this.defaultDriver = defaultDriver;
    }

    public static StorageType value(String value) {
        return Arrays.stream(StorageType.values()).filter(type -> type.getValue().equals(value)).findFirst().orElse(null);
    }

    public String getValue() {
        return name().toLowerCase();
    }

    public StoragePoolDriver getDefaultDriver() {
        return defaultDriver;
    }

}
