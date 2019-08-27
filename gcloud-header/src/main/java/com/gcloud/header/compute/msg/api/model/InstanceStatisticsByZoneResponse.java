package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;
import java.util.List;

public class InstanceStatisticsByZoneResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<InstanceStatisticsZoneItemType> statisticsZoneItem;
	public List<InstanceStatisticsZoneItemType> getStatisticsZoneItem() {
		return statisticsZoneItem;
	}
	public void setStatisticsZoneItem(List<InstanceStatisticsZoneItemType> statisticsZoneItem) {
		this.statisticsZoneItem = statisticsZoneItem;
	}
}
