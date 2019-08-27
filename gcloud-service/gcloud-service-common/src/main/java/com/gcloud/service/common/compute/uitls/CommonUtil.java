package com.gcloud.service.common.compute.uitls;

import com.gcloud.common.util.StringUtils;
import com.gcloud.core.service.SpringUtil;

public class CommonUtil {

	public static <T> T getImplementClass(String classFormat, String param) {
		String classImpl = String.format(classFormat, StringUtils.toUpperCaseFirstOne(param));
		T t = (T) SpringUtil.getBean(classImpl);
		return t;
	}

}
