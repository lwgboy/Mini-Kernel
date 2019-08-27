package com.gcloud.header.network.enums;

import com.google.common.base.CaseFormat;

/**
 * 网卡的状态
 */


public enum PortStatus {

	ACTIVE("激活"),
	DOWN("失活"),
	BUILD("已创建"),
	ERROR("错误"),
	PENDING_CREATE("创建中"),
	PENDING_UPDATE("删除中"),
	PENDING_DELETE("删除中"),
	UNRECOGNIZED("未知");

	private String cnName;

	PortStatus(String cnName) {
		this.cnName = cnName;
	}

	public String value() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
	}

	public String getCnName() {
		return cnName;
	}
}
