package com.gcloud.header.storage.model;

import java.io.Serializable;
import java.util.List;

public class DescribeDiskCategoriesResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<DiskCategoryModel> diskCategory;

	public List<DiskCategoryModel> getDiskCategory() {
		return diskCategory;
	}

	public void setDiskCategory(List<DiskCategoryModel> diskCategory) {
		this.diskCategory = diskCategory;
	}
}
