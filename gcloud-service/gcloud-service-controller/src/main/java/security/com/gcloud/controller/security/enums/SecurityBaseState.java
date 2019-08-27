package com.gcloud.controller.security.enums;

import com.google.common.base.CaseFormat;

/**
 * Created by yaowj on 2018/7/31.
 */
public enum SecurityBaseState {

    WAITING("等待"),
    RUNNING("进行中"),
    SUCCESS("成功"),
    FAILED("失败")

    ;

    SecurityBaseState(String cnName) {
        this.cnName = cnName;
    }

    private String cnName;

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public String getCnName() {
        return cnName;
    }
}
