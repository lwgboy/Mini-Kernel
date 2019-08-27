package com.gcloud.header;

/**
 * Created by yaowj on 2018/9/12.
 */
public class NodeMessage extends GMessage {

    private Boolean success;
    private String errorCode;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
