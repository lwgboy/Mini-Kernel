package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.api.ApiModel;

public class InstanceStatisticsResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description = "统计项")
	private List<InstanceStatisticsItemType> statisticsItem;

	public List<InstanceStatisticsItemType> getStatisticsItem() {
		return statisticsItem;
	}

	public void setStatisticsItem(List<InstanceStatisticsItemType> statisticsItem) {
		this.statisticsItem = statisticsItem;
	}
	
}
