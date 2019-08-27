package com.gcloud.header.image.msg.api;

import com.gcloud.header.ApiReplyMessage;

public class ApiGenDownloadReplyMsg extends ApiReplyMessage {

    private static final long serialVersionUID = 1L;

    private GenDownloadVo downloadInfo;

    public GenDownloadVo getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(GenDownloadVo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

}
