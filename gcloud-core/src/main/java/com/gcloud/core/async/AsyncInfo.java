package com.gcloud.core.async;

import java.util.concurrent.Future;

/**
 * Created by yaowj on 2018/9/26.
 */
public class AsyncInfo {

    private String asyncId;
    private Future<?> future;
    private long timeout;
    private long startTime;

    public AsyncInfo() {
    }

    public AsyncInfo(String asyncId, Future<?> future, long timeout, long startTime) {
        this.asyncId = asyncId;
        this.future = future;
        this.timeout = timeout;
        this.startTime = startTime;
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getAsyncId() {
        return asyncId;
    }

    public void setAsyncId(String asyncId) {
        this.asyncId = asyncId;
    }
}
