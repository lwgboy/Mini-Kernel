package com.gcloud.header.compute.msg.api.vm.statistics;

import java.util.List;

import com.gcloud.header.ListReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsItemType;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsResponse;

public class ApiInstancesStatisticsReplyMsg extends ListReplyMessage<InstanceStatisticsItemType>{
	private static final long serialVersionUID = 1L;
	@ApiModel(description = "统计项")
	InstanceStatisticsResponse statisticsItems;
	@ApiModel(description = "总数量")
	int allNum;
	
	@Override
	public void setList(List<InstanceStatisticsItemType> list) {
		statisticsItems = new InstanceStatisticsResponse();
		statisticsItems.setStatisticsItem(list);
	}

	public InstanceStatisticsResponse getStatisticsItems() {
		return statisticsItems;
	}

	public void setStatisticsItems(InstanceStatisticsResponse statisticsItems) {
		this.statisticsItems = statisticsItems;
	}

	public int getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

}
