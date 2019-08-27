package com.gcloud.controller.network.enums;

import com.google.common.base.CaseFormat;

public enum OvsBridgeRefType {

    PORT("端口");


    private String cnName;

    OvsBridgeRefType(String cnName) {
        this.cnName = cnName;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public String getCnName() {
        return cnName;
    }

}
