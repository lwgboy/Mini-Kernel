/*
 * @Date 2015-5-30
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 是否自启动枚举类
 */
package com.gcloud.header.compute.enums;

public enum AutostartType {
	ENABLE("enable", 1),
	DISABLE("disable", 0),
	NULL(null, -1);
	
	private String name;
	private Integer value;
	AutostartType(String name, Integer value){
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public Integer getValue() {
		return value;
	}

}
