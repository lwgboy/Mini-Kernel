package com.gcloud.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtil {
	private static Map<String, Object> getPrivateFieldNameAndValue(Object obj, List<String> fields) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Class<? extends Object> c = obj.getClass();
		for (Field field : c.getDeclaredFields()) {
			if (field.getModifiers() == Modifier.PRIVATE) {
				Object value = null;
				try {
					boolean previous = field.isAccessible();
					field.setAccessible(true);
					value = field.get(obj);
					field.setAccessible(previous);
					if (fields!= null && fields.contains(field.getName())) {
						map.put(field.getName(), value);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return map;
	}
	
	/**
	 * 
	  * @Title: contrastObject
	  * @Description: 根据字段数组对比两个对象字段值是否相同
	  * @date 2016-9-6 下午4:46:34
	  *
	  * @param oldObj
	  * @param newObj
	  * @param fields
	  * @return  false ： 不相同      true：相同
	 */
	public static boolean contrastObject(Object oldObj, Object newObj, List<String> fields){
		Map<String, Object> oldObjMap = getPrivateFieldNameAndValue(oldObj, fields);
		Map<String, Object> newObjMap = getPrivateFieldNameAndValue(newObj, fields);
		if (fields != null && fields.size() > 0) {
			for (String field : fields) {
				Object oldVal = oldObjMap.get(field);
				Object newVal = newObjMap.get(field);
				if (oldVal != null) {
					if (!oldVal.equals(newVal)) {
						return false;
					}
				} else {
					if (newVal != null) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static String getCalledMethod(){
		String callInfo = "";
		StackTraceElement stack = Thread.currentThread().getStackTrace()[3];
		if(stack != null){
			String className = stack.getClassName();
			String methodName = stack.getMethodName();
			int lineNumber = stack.getLineNumber();
			callInfo = String.format("{%s.%s:%s}", className, methodName, lineNumber);
		}
		return callInfo;
	}
	
}
