package com.gcloud.image.driver;

import com.gcloud.core.service.SpringUtil;

public enum ImageDriverNodeEnum {
	FILE("file", FileStoreNodeDriver.class),
	RBD("rbd", RbdStoreNodeDriver.class);

	private String storageType;
	private Class driverClazz;
	
	ImageDriverNodeEnum(String storageType, Class clazz){
		this.storageType = storageType;
		this.driverClazz = clazz;
	}
	
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	
	public Class getDriverClazz() {
		return driverClazz;
	}

	public void setDriverClazz(Class driverClazz) {
		this.driverClazz = driverClazz;
	}

	public static IImageStoreNodeDriver getByType(String type) {
		for (ImageDriverNodeEnum driverE : ImageDriverNodeEnum.values()) {
			if(driverE.getStorageType().equals(type)) {
				return (IImageStoreNodeDriver)SpringUtil.getBean(driverE.driverClazz);
			}
		}
		return null;
	}
}
