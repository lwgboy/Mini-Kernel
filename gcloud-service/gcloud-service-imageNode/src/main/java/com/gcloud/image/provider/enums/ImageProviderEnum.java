package com.gcloud.image.provider.enums;

import com.gcloud.core.service.SpringUtil;
import com.gcloud.image.provider.IImageProvider;
import com.gcloud.image.provider.impl.*;

public enum ImageProviderEnum {
	GLANCE("glance", GlanceImageNodeProviderImpl.class),
	GCLOUD("gcloud", GcloudImageNodeProviderImpl.class);

	private String providerType;
	private Class providerClazz;
	
	ImageProviderEnum(String providerType, Class clazz){
		this.providerType = providerType;
		this.providerClazz = clazz;
	}
	
	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public Class getProviderClazz() {
		return providerClazz;
	}

	public void setProviderClazz(Class providerClazz) {
		this.providerClazz = providerClazz;
	}


	public static IImageProvider getByType(String type) {
		for (ImageProviderEnum providerE : ImageProviderEnum.values()) {
			if(providerE.getProviderType().equals(type)) {
				return (IImageProvider)SpringUtil.getBean(providerE.providerClazz);
			}
		}
		return null;
	}
}
