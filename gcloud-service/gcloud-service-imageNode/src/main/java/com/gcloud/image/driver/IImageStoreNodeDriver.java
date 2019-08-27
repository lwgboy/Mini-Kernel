package com.gcloud.image.driver;

public interface IImageStoreNodeDriver {
	void downloadImage(String sourceFilePath, String targetFilePath, String imageId);//暂时不考虑image存储后端为lvm的情况
}
