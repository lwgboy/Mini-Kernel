package com.gcloud.controller.network.enums;

import com.google.common.base.CaseFormat;

public enum OvsBridgeState {

    CREATING("创建中"),
    AVAILABLE("可用"),
    CRASHED("异常"),
    DELETING("删除中");


    private String cnName;

    OvsBridgeState(String cnName) {
        this.cnName = cnName;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public String getCnName() {
        return cnName;
    }
}
