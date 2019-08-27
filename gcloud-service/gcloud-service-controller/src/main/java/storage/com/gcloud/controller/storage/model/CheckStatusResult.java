package com.gcloud.controller.storage.model;

/**
 * Created by yaowj on 2018/9/28.
 */
public class CheckStatusResult<T> {

    private Boolean success;
    private T entity;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
