package com.gcloud.controller.image.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcloud.controller.image")
public class ImageProp {
	 @Value("${gcloud.controller.image.stroageType:file}")
	 private String stroageType;//镜像后端存储类型 file\lvm\rbd
	 
	@Value("${gcloud.controller.image.filesystemStoreDir:/var/lib/images/}")
	private String imageFilesystemStoreDir;

	public String getStroageType() {
		return stroageType;
	}

	public void setStroageType(String stroageType) {
		this.stroageType = stroageType;
	}

	public String getImageFilesystemStoreDir() {
		return imageFilesystemStoreDir;
	}

	public void setImageFilesystemStoreDir(String imageFilesystemStoreDir) {
		this.imageFilesystemStoreDir = imageFilesystemStoreDir;
	}
	
}
