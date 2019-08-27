
package com.gcloud.header.storage.msg.api.pool;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.storage.model.DescribeDiskCategoriesResponse;
import com.gcloud.header.storage.model.DiskCategoryModel;

public class ApiDescribeDiskCategoriesReplyMsg extends PageReplyMessage<DiskCategoryModel> {

    private static final long serialVersionUID = 1L;

    private DescribeDiskCategoriesResponse diskCategories;

	@Override
	public void setList(List<DiskCategoryModel> list) {
		diskCategories = new DescribeDiskCategoriesResponse();
		diskCategories.setDiskCategory(list);
	}

	public DescribeDiskCategoriesResponse getDiskCategories() {
		return diskCategories;
	}

	public void setDiskCategories(DescribeDiskCategoriesResponse diskCategories) {
		this.diskCategories = diskCategories;
	}
}
