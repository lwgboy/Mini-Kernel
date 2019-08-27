package com.gcloud.image.provider.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.image.msg.node.DownloadImageMsg;
import com.gcloud.image.driver.IImageStoreNodeDriver;
import com.gcloud.image.driver.ImageDriverNodeEnum;
import com.gcloud.image.prop.ImageNodeProp;
import com.gcloud.image.provider.IImageProvider;
@Component
public class GcloudImageNodeProviderImpl implements IImageProvider {
	@Autowired
    private ImageNodeProp props;
	
	public void downloadImage(DownloadImageMsg msg) {
		IImageStoreNodeDriver driver = (IImageStoreNodeDriver)ImageDriverNodeEnum.getByType(msg.getStroageType());
		if(driver == null) {
			throw new GCloudException("::该类型驱动不存在");
		}
		driver.downloadImage(msg.getImagePath(), props.getImageCachedPath(), msg.getImageId());
	}

}
