package com.gcloud.header.storage.model;

import java.io.Serializable;
import java.util.List;

public class DiskStatusStatisticsResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DiskStatusStatisticsItem> item;

	public List<DiskStatusStatisticsItem> getItem() {
		return item;
	}

	public void setItem(List<DiskStatusStatisticsItem> item) {
		this.item = item;
	}
	
}
