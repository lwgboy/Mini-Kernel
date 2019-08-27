
package com.gcloud.header.image.enums;

public enum ImageStatus {

    UNRECOGNIZED("未知"),
    QUEUED("队列中"),
    SAVING("保存中"),
    ACTIVE("可用"),
    DEACTIVATED("不可用"),
    KILLED("终止"),
    DELETED("已删除"),
    PENDING_DELETE("删除中");

    private String cnName;

    ImageStatus(String cnName) {
        this.cnName = cnName;
    }

    public String value() {
        return name().toLowerCase();
    }

    public String getCnName() {
        return cnName;
    }
}
