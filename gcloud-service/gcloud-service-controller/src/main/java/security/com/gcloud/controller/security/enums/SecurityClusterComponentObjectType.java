package com.gcloud.controller.security.enums;

import java.util.Arrays;

/**
 * Created by yaowj on 2018/7/31.
 */
public enum SecurityClusterComponentObjectType {

    VM("vm", "云服务器"),
    DOCKER_CONTAINER("dc", "Docker容器")

    ;

    private String value;
    private String cnName;

    SecurityClusterComponentObjectType(String value, String cnName) {
        this.value = value;
        this.cnName = cnName;
    }

    public static SecurityClusterComponentObjectType getByValue(String value){
        return Arrays.stream(SecurityClusterComponentObjectType.values()).filter(t -> t.value().equals(value)).findFirst().orElse(null);
    }

    public String value() {
        return value;
    }

    public String getCnName() {
        return cnName;
    }
}
