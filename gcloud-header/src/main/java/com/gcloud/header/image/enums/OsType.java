package com.gcloud.header.image.enums;

import java.util.Arrays;

/**
 * Created by yaowj on 2018/11/22.
 */
public enum OsType {

    LINUX, WINDOWS;

    public String value(){
        return name().toLowerCase();
    }

    public static OsType value(String value){
        return Arrays.stream(OsType.values()).filter(type -> type.value().equals(value)).findFirst().orElse(null);
    }

}
