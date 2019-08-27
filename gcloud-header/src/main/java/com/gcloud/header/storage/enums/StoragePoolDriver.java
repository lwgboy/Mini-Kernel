
package com.gcloud.header.storage.enums;

public enum StoragePoolDriver {

    FILE,
    RBD,
    LVM;

    public static StoragePoolDriver get(String value) {
        if (value != null) {
            for (StoragePoolDriver driver : StoragePoolDriver.values()) {
                if (driver.name().equals(value)) {
                    return driver;
                }
            }
        }
        return null;
    }

}
