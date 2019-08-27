
package com.gcloud.header.storage.enums;

import com.google.common.base.CaseFormat;

public enum VolumeStatus {

    AVAILABLE("可用"),
    ATTACHING("挂载中"),
    CREATING_BACKUP("备份中"),
    CREATING("创建中"),
    DELETING("删除中"),
    DOWNLOADING("下载中"),
    UPLOADING("上传中"),
    ERROR("错误"),
    ERROR_DELETING("删除错误"),
    ERROR_RESTORING("还原错误"),
    IN_USE("已用"),
    RESTORING_BACKUP("备份还原中"),
    DETACHING("卸载中"),
    UNRECOGNIZED("未知"),
    RESIZING("扩展中");

    private String cnName;

    VolumeStatus(String cnName) {
        this.cnName = cnName;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public String getCnName() {
        return cnName;
    }
}
