package com.gcloud.header.compute.enums;

import java.util.Arrays;

public enum DiskProtocol {
    RBD, ISCSI, LVM, FILE;

    public String value(){
        return name().toLowerCase();
    }

    public static DiskProtocol value(String value){
        return Arrays.stream(DiskProtocol.values()).filter(protocol -> protocol.value().equals(value)).findFirst().orElse(null);
    }
}
