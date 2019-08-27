package com.gcloud.header.network.enums;

import com.google.common.base.CaseFormat;

/**
 * 网络的状态
 */


public enum NetworkStatus {

	ACTIVE("激活"),
	DOWN("失活"),
	BUILD("已创建"),
	ERROR("错误"),
	PENDING_CREATE("创建中"),
	PENDING_UPDATE("升级中"),
	PENDING_DELETE("删除中"),
	UNRECOGNIZED("未知");

	private String cnName;

	NetworkStatus(String cnName) {
		this.cnName = cnName;
	}

	public String value() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
	}

	public String getCnName() {
		return cnName;
	}
}
