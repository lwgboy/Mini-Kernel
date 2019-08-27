package com.gcloud.controller.utils;

import java.text.SimpleDateFormat;
import java.util.List;

/*
 * @Date 2015-4-16
 * 
 * @Author yaowj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description sql工具类
 */
public class SqlUtil {
	
	public static String listToInStr(List<String> values){
		
		String valueStr = "";
		for(String value : values){
			valueStr += "'" + value + "',";
		}
		if(!"".equals(valueStr)){
			valueStr = valueStr.substring(0, valueStr.length() - 1);
		}
		
		return valueStr;
		
	}
	

	/**
	  * @Title: inPreStr
	  * @Description: 返回指定数量的in (?, ?) 条件语句
	  * @date 2015-4-16 下午5:31:54
	  *
	  * @param num
	  * @return 形如"?, ?, ?"的字符串
	  */
	public static String inPreStr(int num){
	
		String inStr = "";
		for(int i = 0; i < num; i++){
			inStr += "?,";
		}
		
		if(!"".equals(inStr)){
			inStr = inStr.substring(0, inStr.length() - 1);
		}
		
		return inStr;
	}

	/**
	 * 
	  * @Title: isDateFormat
	  * @Description: 格式化时间
	  * @date 2015-6-11 下午2:20:03
	  *
	  * @param dateStr
	  * @param dateFormat
	  * @return
	 */
	public static boolean isDateFormat(String dateStr, String dateFormat) {
		boolean isDateFormat = true;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			format.setLenient(false);
			format.parse(dateStr);
		}catch (Exception e){
			isDateFormat = false;
		}
		return isDateFormat;

	}
	
	public static String wrap(String name) {
		return "`" + name + "`";
	}
}
