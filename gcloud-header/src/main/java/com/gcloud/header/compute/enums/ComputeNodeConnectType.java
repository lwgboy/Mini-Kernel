package com.gcloud.header.compute.enums;

/**
 * Created by yaowj on 2018/10/22.
 */
public enum ComputeNodeConnectType {

    REGISTER("register"), CONNECT("connect"), DISCONNECT("disconnect");

    private String value;
    ComputeNodeConnectType(String name){
        this.value = name;
    }

    public String getValue() {
        return value;
    }

}
