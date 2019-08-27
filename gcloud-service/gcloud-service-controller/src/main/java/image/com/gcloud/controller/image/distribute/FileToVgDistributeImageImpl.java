package com.gcloud.controller.image.distribute;

import org.springframework.stereotype.Component;

@Component
public class FileToVgDistributeImageImpl implements IDistributeImage {

	@Override
	public void distributeImage(String imageId, String target, String taskId) {
		//记录image_store_info
		//将镜像文件dd到对应的vg上
		//feedback task
	}

	/*@Override
	public void deleteImageCache(String imageId, String target, String taskId) {
		//删掉vg上对应的image cache
	}*/

}
