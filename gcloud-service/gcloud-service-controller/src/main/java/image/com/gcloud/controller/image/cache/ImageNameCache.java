package com.gcloud.controller.image.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class ImageNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		ImageDao imageDao = SpringUtil.getBean(ImageDao.class);
		List<Image> list = imageDao.findAll();
		for(Image item: list){
			result.put(item.getId(),item.getName()==null?item.getId():item.getName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.IMAGE_NAME;
	}

	@Override
	public String getValue(String key) {
		ImageDao imageDao = SpringUtil.getBean(ImageDao.class);
		Image image = imageDao.getById(key);
		return image!=null?(image.getName()==null?image.getId():image.getName()):null;
	}

}
