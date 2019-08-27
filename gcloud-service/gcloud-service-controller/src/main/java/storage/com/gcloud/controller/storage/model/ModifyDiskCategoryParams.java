package com.gcloud.controller.storage.model;

public class ModifyDiskCategoryParams {
	private String id;
	private Integer minSize;
	private Integer maxSize;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getMinSize() {
		return minSize;
	}
	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}
	public Integer getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}
}
