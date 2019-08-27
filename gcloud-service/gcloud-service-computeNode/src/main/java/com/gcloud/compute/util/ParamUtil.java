/*
 * @Date 2015-6-18
 * 
 * @Author zhangzj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 *  参数工具类
 */
package com.gcloud.compute.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.util.SerializeUtils;

public class ParamUtil {

	public static Map<String, Object> stringToMapWithSerialize(String jsonStr) throws GCloudException {
		if (StringUtils.isBlank(jsonStr)) {
			return new HashMap<String, Object>();
		}

		JSONObject jasonObject = JSONObject.parseObject(jsonStr);
		Map<String, Object> map = (Map<String, Object>) jasonObject;

		// 反序列化值
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			Object newValue = SerializeUtils.unSerialize(String.valueOf(value));
			map.put(key, newValue);
		}

		return map;
	}

	public static String mapToStringWithSerialize(Map<String, Object> map) throws GCloudException {
		if (map == null) {
			return null;
		}

		Map<String, Object> result = new HashMap<String, Object>();

		// 序列化值
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			String newValue = SerializeUtils.serialize(value);
			result.put(key, newValue);
		}
		return String.valueOf(JSONObject.toJSONString(result));
	}

	public static Map<String, Object> stringToMap(String jsonStr) {
		if (StringUtils.isBlank(jsonStr)) {
			return new HashMap<String, Object>();
		}

		JSONObject jasonObject = JSONObject.parseObject(jsonStr);
		Map<String, Object> map = (Map<String, Object>) jasonObject;

		return map;
	}

	public static String mapToString(Map<String, Object> map) {
		if (map == null) {
			return null;
		}
		return String.valueOf(JSONObject.toJSONString(map));
	}

	public static String getSingleString(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			return params.get(key).toString();
		}
		return null;
	}

	public static Object getListObject(String key, Map<String, Object> params) {
		return params.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getListString(String key, Map<String, Object> params) {
		return (List<String>) params.get(key);
	}

	public static Long getSingleLong(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			return Long.parseLong(params.get(key).toString());
		}
		return null;
	}

	public static Float getSingleFloat(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			return Float.parseFloat(params.get(key).toString());
		}
		return null;
	}

	public static List<Float> getListFloat(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			List<Float> list = new ArrayList<Float>();
			@SuppressWarnings("unchecked")
			List<String> tmps = (List<String>) params.get(key);
			for (String value : tmps) {
				list.add(Float.parseFloat(value));
			}
			return list;
		}
		return null;
	}

	public static List<Long> getListLong(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			List<Long> list = new ArrayList<Long>();
			@SuppressWarnings("unchecked")
			List<String> tmps = (List<String>) params.get(key);
			for (String value : tmps) {
				list.add(Long.parseLong(value));
			}
			return list;
		}
		return null;
	}

	public static Integer getSingleInteger(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			return Integer.parseInt(params.get(key).toString());
		}
		return null;
	}

	public static List<Integer> getListInteger(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			List<Integer> list = new ArrayList<Integer>();
			@SuppressWarnings("unchecked")
			List<String> tmps = (List<String>) params.get(key);
			for (String value : tmps) {
				list.add(Integer.parseInt(value));
			}
			return list;
		}
		return null;
	}

	public static Boolean getSingleBoolean(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			return Boolean.parseBoolean(params.get(key).toString());
		}
		return null;
	}

	public static JSONObject getJsonObject(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			return JSON.parseObject(params.get(key).toString());
		}
		return null;
	}

	public static JSONArray getJsonArray(String key, Map<String, Object> params) {
		if (params.containsKey(key) && params.get(key) != null) {
			return JSON.parseArray(params.get(key).toString());
		}
		return null;
	}

	public static Date getSingleDate(String key, Map<String, Object> params) {
		String date = getSingleString(key, params);
		if (StringUtils.isNotBlank(date)) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getAllParamAsString(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (params != null) {
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String name = iterator.next();
				String value = null;

				Object o = params.get(name);
				if (o instanceof String[]) {// 忽略struts.xml中配置的region、module、action
											// 3个参数
					String[] values = (String[]) o;
					if (values != null && values.length > 0 && StringUtils.isNotBlank(values[0])) {
						value = values[0];
					}
					if (value == null) {
						continue;
					}

					ParamNameType paramNameType = getPatternParamName(name);
					String paramName = paramNameType.getParamName();
					Boolean isArray = paramNameType.getIsArray();
					if (isArray) {
						/*
						 * if (result.containsKey(paramName)) {
						 * ((List<String>)result.get(paramName)).add(value); }
						 * else { List<String> list = new ArrayList<String>();
						 * list.add(value); result.put(paramName, list); }
						 */
						List<String> list = new ArrayList<String>();
						for (String val : values) {
							list.add(val);
						}
						if (result.containsKey(paramName)) {
							((List<String>) result.get(paramName)).addAll(list);
						} else {
							result.put(paramName, list);
						}
					} else {
						result.put(paramName, value);
					}
				}
			}
		}
		return result;
	}

	private static ParamNameType getPatternParamName(String name) {
		String regex = "((\\w|\\.)+)\\[\\d*\\]";

		String paramName = name;
		Boolean isArray = false;

		if (name.matches(regex)) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(name);
			while (matcher.find()) {
				paramName = matcher.group(1);
				break;
			}
			isArray = true;
		}
		ParamNameType paramNameType = new ParamNameType();
		paramNameType.setParamName(paramName);
		paramNameType.setIsArray(isArray);
		return paramNameType;
	}

	private static class ParamNameType {
		private String paramName;
		private Boolean isArray;

		public String getParamName() {
			return paramName;
		}

		public void setParamName(String paramName) {
			this.paramName = paramName;
		}

		public Boolean getIsArray() {
			return isArray;
		}

		public void setIsArray(Boolean isArray) {
			this.isArray = isArray;
		}

	}
	
	public static <T> List<T> getList(JSONObject jsonObject, String param, Class<T> clazz){
	
		if(StringUtils.isBlank(param)){
			return null;
		}
		
		JSONArray jsonArr = jsonObject.getJSONArray(param);
		if(jsonArr == null){
			return null;
		}
		
		return JSONObject.parseArray(jsonArr.toJSONString(), clazz);
		
		
	}
	
	
}
