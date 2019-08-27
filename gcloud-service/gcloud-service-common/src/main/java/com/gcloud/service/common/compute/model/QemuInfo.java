package com.gcloud.service.common.compute.model;

public class QemuInfo {
    private long virtualSize;  // 单位字节
    private String format;
    private String backingFile;

    public QemuInfo() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getVirtualSize() {
        return virtualSize;
    }

    public void setVirtualSize(long virtualSize) {
        this.virtualSize = virtualSize;
    }

    //向上取整
    public int virtualSizeGb(){
        return (int)Math.ceil(virtualSize / 1024d / 1024 / 1024);
    }

    public String getBackingFile() {
        return backingFile;
    }

    public void setBackingFile(String backingFile) {
        this.backingFile = backingFile;
    }
}
