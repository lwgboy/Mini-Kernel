package com.gcloud.header.compute.enums;

import java.util.Arrays;

public enum FileFormat {
	
	QCOW2("qcow2"),
    VHD("vhd"),
    RAW("raw");
    private String value;
    
    FileFormat(String value){
        this.value=value;
    }
    public String getValue(){
        return this.value;
    }

    public static FileFormat value(String value){
        return Arrays.stream(FileFormat.values()).filter(format -> format.getValue().equals(value)).findFirst().orElse(null);
    }
	
}
