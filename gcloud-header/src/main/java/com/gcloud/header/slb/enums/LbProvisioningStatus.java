package com.gcloud.header.slb.enums;

import com.google.common.base.CaseFormat;

public enum LbProvisioningStatus {

    ACTIVE("激活"),
    DOWN("失活"),
    CREATED("已创建"),
    PENDING_CREATE("创建中"),
    PENDING_UPDATE("升级中"),
    PENDING_DELETE("删除中"),
    INACTIVE("失活"),
    ERROR("错误");


    private String cnName;

    LbProvisioningStatus(String cnName) {
        this.cnName = cnName;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public String getCnName() {
        return cnName;
    }

}
