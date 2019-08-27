package com.gcloud.common.model;

/**
 * Created by yaowj on 2018/9/28.
 */
public class PageParams {

    private Integer pageNumber = 1;
    private Integer pageSize = 10;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
