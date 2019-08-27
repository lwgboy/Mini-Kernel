package com.gcloud.header.storage.enums;

/**
 * Created by yaowj on 2018/10/8.
 */
public enum  DiskType {

    SYSTEM("system", "系统盘"), DATA("data", "数据盘");

    private String value;
    private String cnName;


    DiskType(String value, String cnName) {
        this.value = value;
        this.cnName = cnName;
    }

    public String getValue() {
        return value;
    }

    public String getCnName() {
        return cnName;
    }

}
