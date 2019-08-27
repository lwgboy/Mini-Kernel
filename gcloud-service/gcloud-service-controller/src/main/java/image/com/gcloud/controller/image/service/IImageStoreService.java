package com.gcloud.controller.image.service;

import java.util.List;
import java.util.Map;

import com.gcloud.controller.image.entity.ImageStore;

public interface IImageStoreService {
	void update(ImageStore store, List<String> fields);
	void delete(ImageStore store);
	ImageStore findUniqueByProperties(Map<String, Object> props);
}
