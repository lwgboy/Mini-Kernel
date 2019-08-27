package com.gcloud.controller.storage.model;

public class CreateDiskCategoryParams {
    private String name;
    private Integer minSize;
    private Integer maxSize;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
