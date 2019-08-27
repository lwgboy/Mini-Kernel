package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.api.ApiModel;

public class InstanceStatisticsZoneItemType  implements Serializable {
	@ApiModel(description = "可用区ID")
	private String zoneId;
	@ApiModel(description = "可用区名")
	private String zoneName;
	@ApiModel(description = "统计列表")
	private List<InstanceStatisticsItemType> statisticsItems;
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public List<InstanceStatisticsItemType> getStatisticsItems() {
		return statisticsItems;
	}
	public void setStatisticsItems(List<InstanceStatisticsItemType> statisticsItems) {
		this.statisticsItems = statisticsItems;
	}
}
