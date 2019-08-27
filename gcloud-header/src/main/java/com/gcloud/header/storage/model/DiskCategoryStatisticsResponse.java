package com.gcloud.header.storage.model;

import java.io.Serializable;
import java.util.List;

public class DiskCategoryStatisticsResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DiskCategoryStatisticsItem> item;

	public List<DiskCategoryStatisticsItem> getItem() {
		return item;
	}

	public void setItem(List<DiskCategoryStatisticsItem> item) {
		this.item = item;
	}
	
}
