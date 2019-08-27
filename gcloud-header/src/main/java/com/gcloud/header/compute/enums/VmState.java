package com.gcloud.header.compute.enums;

import com.google.common.base.CaseFormat;

public enum VmState {
	STOPPED("已关机"),
	PAUSED("已停止"),
	RUNNING("正在运行"),
	DISABLED("已故障"),
	CRASHED("已异常"),
	PENDING("正在创建");


	private String cnName;

	VmState(String cnName) {
		this.cnName = cnName;
	}

	public String value() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
	}

	public String getCnName() {
		return cnName;
	}
}
