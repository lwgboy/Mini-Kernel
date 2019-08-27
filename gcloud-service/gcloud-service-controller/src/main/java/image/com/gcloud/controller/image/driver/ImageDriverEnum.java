package com.gcloud.controller.image.driver;

import com.gcloud.core.service.SpringUtil;

public enum ImageDriverEnum {
	FILE("file", FileStoreDriver.class),
	RBD("rbd", RbdStoreDriver.class);

	private String storageType;
	private Class clazz;
	
	ImageDriverEnum(String storageType, Class clazz){
		this.storageType = storageType;
		this.clazz = clazz;
	}
	
	public static IImageStoreDriver getByType(String type) {
		for (ImageDriverEnum driverE : ImageDriverEnum.values()) {
			if(driverE.getStorageType().equals(type)) {
				return (IImageStoreDriver)SpringUtil.getBean(driverE.clazz);
			}
		}
		return null;
	}
	
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
}
