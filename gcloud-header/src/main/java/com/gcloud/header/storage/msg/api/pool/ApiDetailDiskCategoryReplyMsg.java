package com.gcloud.header.storage.msg.api.pool;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.storage.model.DiskCategoryModel;

public class ApiDetailDiskCategoryReplyMsg extends ApiReplyMessage{

	private static final long serialVersionUID = 1L;
	
	private DiskCategoryModel diskCategory;

	public DiskCategoryModel getDiskCategory() {
		return diskCategory;
	}

	public void setDiskCategory(DiskCategoryModel diskCategory) {
		this.diskCategory = diskCategory;
	}
}
