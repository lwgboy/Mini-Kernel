package com.gcloud.controller.security.enums;

import com.google.common.base.CaseFormat;

import java.util.Arrays;

/**
 * Created by yaowj on 2018/7/30.
 */
public enum SecurityNetworkType {

    PROTECTION("防护网", true),
    MANAGEMENT("管理网", true),
    OUTER("外网", false),
    HA("HA心跳网", true);

    private String cnName;
    private Boolean needSecurityGroup;

    SecurityNetworkType(String cnName, Boolean needSecurityGroup) {
        this.cnName = cnName;
        this.needSecurityGroup = needSecurityGroup;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public String getName() {
        return cnName;
    }

    public Boolean getNeedSecurityGroup() {
        return needSecurityGroup;
    }

    public static SecurityNetworkType getByValue(String value){
        return Arrays.stream(SecurityNetworkType.values()).filter(t -> t.value().equals(value)).findFirst().orElse(null);
    }
}
