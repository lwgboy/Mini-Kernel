package com.gcloud.controller.image.distribute;

import com.gcloud.core.service.SpringUtil;

public enum ImageDistributeEnum {
	FILE_TO_NODE("file", "node", FileToNodeDistributeImageImpl.class),
	FILE_TO_VG("file", "vg", FileToVgDistributeImageImpl.class),
	FILE_TO_RBD("file", "rbd", FileToRbdDistributeImageImpl.class);

	private String storageType;
	private String target;
	private Class clazz;
	
	ImageDistributeEnum(String storageType, String target, Class clazz){
		this.storageType = storageType;
		this.target = target;
		this.clazz = clazz;
	}
	
	public static IDistributeImage getByType(String type, String target) {
		for (ImageDistributeEnum driverE : ImageDistributeEnum.values()) {
			if(driverE.getStorageType().equals(type.toLowerCase()) && driverE.getTarget().equals(target)) {
				return (IDistributeImage)SpringUtil.getBean(driverE.clazz);
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
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
