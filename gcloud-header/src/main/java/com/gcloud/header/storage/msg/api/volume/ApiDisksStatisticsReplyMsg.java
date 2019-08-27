package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.storage.model.DiskCategoryStatisticsResponse;
import com.gcloud.header.storage.model.DiskStatusStatisticsResponse;

public class ApiDisksStatisticsReplyMsg extends ApiReplyMessage{
	private static final long serialVersionUID = 1L;
	private int allNum;
	private DiskStatusStatisticsResponse statusStatisticsItems;
	private DiskCategoryStatisticsResponse categoryStatisticsItems;
	public int getAllNum() {
		return allNum;
	}
	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}
	public DiskStatusStatisticsResponse getStatusStatisticsItems() {
		return statusStatisticsItems;
	}
	public void setStatusStatisticsItems(DiskStatusStatisticsResponse statusStatisticsItems) {
		this.statusStatisticsItems = statusStatisticsItems;
	}
	public DiskCategoryStatisticsResponse getCategoryStatisticsItems() {
		return categoryStatisticsItems;
	}
	public void setCategoryStatisticsItems(DiskCategoryStatisticsResponse categoryStatisticsItems) {
		this.categoryStatisticsItems = categoryStatisticsItems;
	}
	
}
