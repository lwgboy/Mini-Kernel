package com.gcloud.header.compute.enums;

import com.google.common.base.CaseFormat;

public enum ImageOwnerType {
	SYSTEM("公共镜像"), SELF("自定义镜像"), SHARED("共享镜像"), MARKETPLACE("服务市场");

	private String cnName;

	ImageOwnerType(String cnName) {
		this.cnName = cnName;
	}

	public String value() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
	}

	public String getCnName() {
		return cnName;
	}
}
