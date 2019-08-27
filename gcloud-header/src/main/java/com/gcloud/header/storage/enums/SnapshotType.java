
package com.gcloud.header.storage.enums;

public enum SnapshotType {

    EXTERNAL,
    INTERNAL;

    public static SnapshotType get(String value) {
        for (SnapshotType type : SnapshotType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

}
