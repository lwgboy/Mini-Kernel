package com.gcloud.header.compute.enums;

import com.google.common.base.CaseFormat;

/**
 * Created by yaowj on 2018/11/12.
 */
public enum VmTaskStateType {
    TASK, WORKFLOW_TASK;

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }
}
