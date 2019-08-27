package com.gcloud.header.compute.enums;

import com.google.common.base.CaseFormat;

/**
 * Created by yaowj on 2018/11/12.
 */
public enum VmStepState {

    STOPPING("正在关机"),
    REBOOTING("正在强制重启"),
    STARTING("正在开机")
    ;

    VmStepState(String cnName) {
        this.cnName = cnName;
    }

    private String cnName;

    public String getCnName() {
        return cnName;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }



}
