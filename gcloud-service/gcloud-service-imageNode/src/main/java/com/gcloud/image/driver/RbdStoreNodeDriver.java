package com.gcloud.image.driver;

import org.springframework.stereotype.Component;


@Component
public class RbdStoreNodeDriver  implements IImageStoreNodeDriver {
	/*@Autowired
	ImageNodeProp prop;*/

	@Override
	public void downloadImage(String sourceFilePath, String targetFilePath, String imageId) {
		//rbd copy image 到 targetFilePath
	}

}
