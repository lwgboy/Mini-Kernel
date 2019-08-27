package com.gcloud.header;

import java.util.List;

import com.gcloud.framework.db.PageResult;

public abstract class PageReplyMessage<E> extends ApiReplyMessage{
	private Integer pageSize;
	private Integer pageNumber;
	private Integer totalCount;
	
	public abstract void setList(List<E> list);
	
	public void init(PageResult<E> pageResult){
		this.pageSize=pageResult.getPageSize();
		this.pageNumber=pageResult.getPageNo();
		this.totalCount=pageResult.getTotalCount();
		setList(pageResult.getList());
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
}
