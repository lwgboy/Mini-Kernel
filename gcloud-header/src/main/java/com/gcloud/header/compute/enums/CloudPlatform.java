package com.gcloud.header.compute.enums;

import org.apache.commons.lang.StringUtils;

/*
 * @Date 2015-5-19
 * 
 * @Author zhangzj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 云平台类型
 */
public enum CloudPlatform {
	DESKTOPGCLOUD("DesktopCloud"), IDCSEVER("IDCSever");
	private CloudPlatform(String value) {
		this.value = value;
	}
	
	public static CloudPlatform getPlatformByValue(String val){
		
		CloudPlatform result = null;
		if(!StringUtils.isBlank(val)){
			CloudPlatform[] platformArr = CloudPlatform.values();
			for(CloudPlatform platform : platformArr){
				if(val.equals(platform.getValue())){
					result = platform;
					break;
				}
			}
		}
		return result;
		
	}

	private String value;

	public String getValue() {
		return value;
	}
}
