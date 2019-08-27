package com.gcloud.core.util;

import java.util.List;

public class SqlUtil {
	public static String listToInStr(List<String> values) {
		String valueStr = "";
		for (String value : values) {
			valueStr += "'" + value + "',";
		}
		if (!"".equals(valueStr)) {
			valueStr = valueStr.substring(0, valueStr.length() - 1);
		}
		return valueStr;
	}
	
	/**
	 * @Title: inPreStr
	 * @Description: 返回指定数量的in (?, ?) 条件语句
	 * 
	 * @param num
	 * @return 形如"?, ?, ?"的字符串
	 */
	public static String inPreStr(int num) {

		String inStr = "";
		for (int i = 0; i < num; i++) {
			inStr += "?,";
		}

		if (!"".equals(inStr)) {
			inStr = inStr.substring(0, inStr.length() - 1);
		}

		return inStr;
	}
}
