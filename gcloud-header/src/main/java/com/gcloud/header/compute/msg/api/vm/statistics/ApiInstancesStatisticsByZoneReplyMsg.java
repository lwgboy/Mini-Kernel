package com.gcloud.header.compute.msg.api.vm.statistics;

import java.util.List;

import com.gcloud.header.ListReplyMessage;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsByZoneResponse;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsZoneItemType;

public class ApiInstancesStatisticsByZoneReplyMsg extends ListReplyMessage<InstanceStatisticsZoneItemType>{

	private static final long serialVersionUID = 1L;
	InstanceStatisticsByZoneResponse statisticsByZoneItems;
	
	@Override
	public void setList(List<InstanceStatisticsZoneItemType> list) {
		statisticsByZoneItems = new InstanceStatisticsByZoneResponse();
		statisticsByZoneItems.setStatisticsZoneItem(list);
	}

	public InstanceStatisticsByZoneResponse getStatisticsByZoneItems() {
		return statisticsByZoneItems;
	}

	public void setStatisticsByZoneItems(InstanceStatisticsByZoneResponse statisticsByZoneItems) {
		this.statisticsByZoneItems = statisticsByZoneItems;
	}

}
