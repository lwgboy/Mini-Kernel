package com.gcloud.core.async;

/**
 * Created by yaowj on 2018/10/9.
 */
public class AsyncResult {

    private AsyncStatus asyncStatus;
    private String errorMsg;

    public AsyncResult() {
    }

    public AsyncResult(AsyncStatus asyncStatus) {
        this.asyncStatus = asyncStatus;
    }

    public AsyncStatus getAsyncStatus() {
        return asyncStatus;
    }

    public void setAsyncStatus(AsyncStatus asyncStatus) {
        this.asyncStatus = asyncStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
