package com.gcloud.header.image.msg.api;

import java.util.List;

import com.gcloud.header.ListReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.image.model.ImageStatisticsItem;
import com.gcloud.header.image.model.ImageStatisticsResponse;

public class ApiImagesStatisticsReplyMsg extends ListReplyMessage<ImageStatisticsItem>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageStatisticsResponse statisticsItems;
	@ApiModel(description = "总数量")
	int allNum;
	
	@Override
	public void setList(List<ImageStatisticsItem> list) {
		statisticsItems = new ImageStatisticsResponse();
		statisticsItems.setStatisticsItem(list);
	}
	public ImageStatisticsResponse getStatisticsItems() {
		return statisticsItems;
	}
	public void setStatisticsItems(ImageStatisticsResponse statisticsItems) {
		this.statisticsItems = statisticsItems;
	}
	public int getAllNum() {
		return allNum;
	}
	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}
	
}
