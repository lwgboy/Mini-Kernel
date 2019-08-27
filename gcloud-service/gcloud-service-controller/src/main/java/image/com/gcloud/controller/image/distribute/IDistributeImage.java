package com.gcloud.controller.image.distribute;

/**
 * 各种镜像存储后端分发镜像
 *
 */
public interface IDistributeImage {
	void distributeImage(String imageId, String target, String taskId);
	
//	void deleteImageCache(String imageId, String target,String storeType, String taskId);
}
