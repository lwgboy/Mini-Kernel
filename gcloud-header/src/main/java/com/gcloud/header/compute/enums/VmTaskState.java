/*
 * @Date 2015-4-16
 *
 * @Author chenyu1@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description 云服务器状态常量
 */
package com.gcloud.header.compute.enums;

import com.google.common.base.CaseFormat;

public enum VmTaskState {

	// 任务状态
	PENDING("正在创建"),
	DELETING("正在删除"),
	LOGIC_DELETEING("正在逻辑删除"),
	ATTACH_NETCARD("正在挂载网卡"),
	DETACH_NETCARD("正在卸载网卡"),
	ATTACH_DISK("正在挂载磁盘"),
	DETACH_DISK("正在卸载磁盘"),
	BUNDLING("正在打包"),
	MODIFYING_CONFIG("正在修改配置")
	;


	private String cnName;

	VmTaskState(String cnName) {
		this.cnName = cnName;
	}

	public String value() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
	}


	public String getCnName() {
		return cnName;
	}
}
