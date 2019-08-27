package com.gcloud.controller.image.driver;


/**
 * image存储后端
 *
 */
public interface IImageStoreDriver {
	void copyImage(String sourceFilePath, String imageId);//暂时不考虑image存储后端为lvm的情况
	void deleteImage(String imageId);
	/**分发镜像
	 * @param imageId
	 * @param target  可为节点名、vg名
	 * @param targetType node、vg
	 */
	void distributeImage(String imageId, String target, String targetType, String taskId);
	
	/** 获取镜像文件实际大小，单位 byte
	 * @param imageId
	 * @return
	 */
	long getImageActualSize(String imageId);
	
//	void deleteImageCache(String imageId);
	
}
