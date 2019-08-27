package com.gcloud.header;

public abstract class ApiPageMessage extends APISyncCallMessage {
	private Integer pageNumber = 1;
	// 页大小(默认一页十条记录)
	private Integer pageSize = 10;
	// 排序字段
	private String sort;
	// 排序方式：ASC/DESC
	private String dir;
	// 搜索关键字
	private String key;
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
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
