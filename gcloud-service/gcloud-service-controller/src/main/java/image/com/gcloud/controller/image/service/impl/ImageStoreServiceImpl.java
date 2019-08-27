package com.gcloud.controller.image.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.image.dao.ImageStoreDao;
import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.controller.image.service.IImageStoreService;

@Service
public class ImageStoreServiceImpl implements IImageStoreService {
	@Autowired
	ImageStoreDao storeDao;

	@Override
	public void update(ImageStore store, List<String> fields) {
		storeDao.update(store, fields);
	}
	
	@Override
	public ImageStore findUniqueByProperties(Map<String, Object> props) {
		return storeDao.findUniqueByProperties(props);
	}
	
	@Override
	public void delete(ImageStore store) {
		storeDao.delete(store);
	}
}
