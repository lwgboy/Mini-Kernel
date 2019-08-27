package com.gcloud.header;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ReplyMessage extends GMessage {

    @JsonIgnore
    private Boolean success;
    @JsonIgnore
    private String errorMsg;
    @JsonIgnore
    private String[] errorParam;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String[] getErrorParam() {
        return errorParam;
    }

    public void setErrorParam(String[] errorParam) {
        this.errorParam = errorParam;
    }
}
