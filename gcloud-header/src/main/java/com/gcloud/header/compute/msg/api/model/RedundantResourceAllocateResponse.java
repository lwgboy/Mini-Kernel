package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.api.ApiModel;

public class RedundantResourceAllocateResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description = "分配项")
	private List<RedundantResourceAllocateItem> allocateItem;

	public List<RedundantResourceAllocateItem> getAllocateItem() {
		return allocateItem;
	}

	public void setAllocateItem(List<RedundantResourceAllocateItem> allocateItem) {
		this.allocateItem = allocateItem;
	}
	
}
