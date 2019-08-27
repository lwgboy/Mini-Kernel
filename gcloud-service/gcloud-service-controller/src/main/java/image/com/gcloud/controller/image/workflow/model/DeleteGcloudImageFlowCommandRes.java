package com.gcloud.controller.image.workflow.model;

import java.util.List;

import com.gcloud.controller.image.entity.ImageStore;

public class DeleteGcloudImageFlowCommandRes {
	List<ImageStore> stores;

	public List<ImageStore> getStores() {
		return stores;
	}

	public void setStores(List<ImageStore> stores) {
		this.stores = stores;
	}
	
}
