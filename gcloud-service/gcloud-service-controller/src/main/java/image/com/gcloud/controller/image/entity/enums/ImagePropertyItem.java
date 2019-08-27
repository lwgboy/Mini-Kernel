package com.gcloud.controller.image.entity.enums;

import com.google.common.base.CaseFormat;

/**
 * Created by yaowj on 2018/11/22.
 */
public enum  ImagePropertyItem {
    ARCHITECTURE,
    OS_TYPE,
    DESCRIPTION;

    public String value() {
        return name().toLowerCase();
    }


}
