package com.gcloud.controller.security.enums;

import com.google.common.base.CaseFormat;

import java.util.Arrays;

/**
 * Created by yaowj on 2018/7/30.
 */
public enum SecurityNetcardConfigType {

    NORMAL("normal"),OVS_BRIDGE("ovs-bridge");

    private String value;

    SecurityNetcardConfigType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public static SecurityNetcardConfigType getByValue(String value){
        return Arrays.stream(SecurityNetcardConfigType.values()).filter(s -> s.value().equals(value)).findFirst().orElse(null);
    }
}
