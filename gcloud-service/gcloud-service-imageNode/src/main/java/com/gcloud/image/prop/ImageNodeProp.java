package com.gcloud.image.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcloud.image-node")
public class ImageNodeProp {
	@Value("${gcloud.image-node.controller:}")
	private String controller;
	
	@Value("${gcloud.image-node.imageCachedPath:/var/lib/gcloud/caches/images/}")
	private String imageCachedPath;

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getImageCachedPath() {
		return imageCachedPath;
	}

	public void setImageCachedPath(String imageCachedPath) {
		this.imageCachedPath = imageCachedPath;
	}
	
}
