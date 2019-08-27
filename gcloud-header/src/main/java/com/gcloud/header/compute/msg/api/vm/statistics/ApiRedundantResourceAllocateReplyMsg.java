package com.gcloud.header.compute.msg.api.vm.statistics;

import java.util.List;

import com.gcloud.header.ListReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.compute.msg.api.model.RedundantResourceAllocateItem;
import com.gcloud.header.compute.msg.api.model.RedundantResourceAllocateResponse;

public class ApiRedundantResourceAllocateReplyMsg extends ListReplyMessage<RedundantResourceAllocateItem>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description = "统计项")
	RedundantResourceAllocateResponse allocateItems;

	public RedundantResourceAllocateResponse getAllocateItems() {
		return allocateItems;
	}

	public void setAllocateItems(RedundantResourceAllocateResponse allocateItems) {
		this.allocateItems = allocateItems;
	}

	@Override
	public void setList(List<RedundantResourceAllocateItem> list) {
		allocateItems = new RedundantResourceAllocateResponse();
		allocateItems.setAllocateItem(list);
	}
}
