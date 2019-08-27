package com.gcloud.header.image.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.api.ApiModel;


public class ImageStatisticsResponse  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description = "统计项")
	private List<ImageStatisticsItem> statisticsItem;

	public List<ImageStatisticsItem> getStatisticsItem() {
		return statisticsItem;
	}

	public void setStatisticsItem(List<ImageStatisticsItem> statisticsItem) {
		this.statisticsItem = statisticsItem;
	}
	
}
