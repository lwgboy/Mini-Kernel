/*
 * @Date 2015-4-16
 * 
 * @Author zhangzj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 容错状态常量
 */
package com.gcloud.header.compute.enums;

public enum FtState {
	CHECKPOINTING("checkpointing", 1),
	FAILED("failed", 2),
	ACTIVE("active", 0),
	NO_FT("no_fault_tolreant", 3);
	
	private String name;
	private Integer value;

	FtState(String name, Integer value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}
	
	/**
	 * 
	 * @Title: getName
	 * @Description: 根据value获取name
	 * @date 2015-4-25 上午11:31:30
	 *
	 * @param value
	 * @return
	 */
	public static String getName(Integer value) {
		for (FtState ft : FtState.values()) {
			if (ft.getValue().equals(value)) {
				return ft.name;
			}
		}
		return null;
	}
	
}
