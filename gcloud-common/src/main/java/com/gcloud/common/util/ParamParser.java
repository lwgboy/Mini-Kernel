package com.gcloud.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("unchecked")
public class ParamParser {

	public static String getSingleString(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			return params.get(key).toString();
		}
		return null;
	}

	public static List<String> getListString(String key, Map<String, Object> params) {
		if(params.get(key)==null)
		{
			return new ArrayList<String>();
		}
		return (List<String>) params.get(key);
	}

	public static Long getSingleLong(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			return Long.parseLong(params.get(key).toString());
		}
		return null;
	}
	
	public static Float getSingleFloat(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			return Float.parseFloat(params.get(key).toString());
		}
		return null;
	}
	
	public static List<Float> getListFloat(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			List<Float> list=new ArrayList<Float>();
			List<String> tmps=(List<String>) params.get(key);
			for(String value:tmps){
				list.add(Float.parseFloat(value));
			}
			return list;
		}
		return null;
	}

	public static List<Long> getListLong(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			List<Long> list=new ArrayList<Long>();
			List<String> tmps=(List<String>) params.get(key);
			for(String value:tmps){
				list.add(Long.parseLong(value));
			}
			return list;
		}
		return null;
	}
	
	public static Integer getSingleInteger(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			return Integer.parseInt(params.get(key).toString());
		}
		return null;
	}

	public static List<Integer> getListInteger(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			List<Integer> list=new ArrayList<Integer>();
			List<String> tmps=(List<String>) params.get(key);
			for(String value:tmps){
				list.add(Integer.parseInt(value));
			}
			return list;
		}
		return null;
	}
	
	public static Boolean getSingleBoolean(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			return Boolean.parseBoolean(params.get(key).toString());
		}
		return null;
	}

	public static JSONObject getJsonObject(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			return JSON.parseObject(params.get(key).toString());
		}
		return null;
	}
	
	public static JSONArray getJsonArray(String key, Map<String, Object> params) {
		if (params.containsKey(key)) {
			return JSON.parseArray(params.get(key).toString());
		}
		return null;
	}
	
	public static Date getSingleDate(String key, Map<String, Object> params){
		String date=getSingleString(key, params);
		if(StringUtils.isNotBlank(date)){
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
