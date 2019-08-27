package com.gcloud.controller.image.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class ImageResourceIsolationCheckImpl extends ResourceIsolationCheckImpl {
	@Autowired
    private ImageDao imageDao;
	
	@Override
	public String getResourceTenantId(String resourceId) {
		Image image = imageDao.getById(resourceId);
		return image==null?null:image.getTenantId();
	}

}
