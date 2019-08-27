package com.gcloud.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.gcloud.core.exception.GCloudException;
import org.apache.commons.beanutils.BeanUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class BeanUtil {
	public static Map<String, Object> convertBeanToMap(Object obj) {
		if(obj == null) {
			return null;
		}        
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			log.error("transBean2Map Error " + e.getMessage());
		}
		return map;
	}
	
	/**
     * 将 Map对象转化为JavaBean
     * @param map
     * @param T
     * @return
     * @throws Exception
     */
    public static <T> T convertMapToBean(Map<String, Object> map, Class<T> T) {
        if (map == null || map.size() == 0)
        {
            return null;
        }
        T bean = null;
        try {
	        BeanInfo beanInfo = Introspector.getBeanInfo(T);
	        bean = T.newInstance();
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	        for (int i = 0, n = propertyDescriptors.length; i < n; i++)
	        {
	            PropertyDescriptor descriptor = propertyDescriptors[i];
	            String propertyName = descriptor.getName();
	            
	            if (map.keySet().contains(propertyName))
	            {
	                Object value = map.get(propertyName);
	                //这个方法不会报参数类型不匹配的错误。
	                BeanUtils.copyProperty(bean, propertyName, value);
	            }
	        }
        }catch(Exception e) {
        	log.error("convertMapToBean " + e.getMessage());
        }
        return bean;
    }
	
	 /**
     * 将 List<Map>对象转化为List<JavaBean>
     * @param listMap
     * @param T
     * @return
     * @throws Exception
     */
    public static <T> List<T> convertListMapToListBean(List<Map<String, Object>> listMap, Class<T> T) {
        List<T> beanList = new ArrayList<>();
        if (listMap != null && !listMap.isEmpty())
        {
            for (int i = 0, n = listMap.size(); i < n; i++)
            {
                Map<String, Object> map = listMap.get(i);
                T bean = convertMapToBean(map, T);
                beanList.add(bean);
            }
            return beanList;
        }
        return beanList;
    }
	
	public static Object getObjectByFieldName(Object obj, String name) {
		char[] ch = name.toCharArray();
	    if (ch[0] >= 'a' && ch[0] <= 'z') {
	        ch[0] = (char) (ch[0] - 32);
	    }
		String getter = "get" + new String(ch); 
        try {  
            // 通过method的反射方法获取其属性值  
            Method method = obj.getClass().getMethod(getter, new Class[]{});  
            return method.invoke(obj, new Object[]{});  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } catch (InvocationTargetException e) {  
            e.printStackTrace();  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (NoSuchMethodException e) {  
            e.printStackTrace();  
        } 
        return null;
	}


	public static <D> D copyProperties(Object orig, Class<D> dest) throws GCloudException{

    	D d = null;
    	try{
    		d = dest.newInstance();
    		BeanUtils.copyProperties(d, orig);
		}catch (Exception ex){
			log.error("copy properties error", ex);
			throw new GCloudException("::param error");
		}

		return d;
	}

	public static <D> D copyBean(Object orig, Class<D> dest){
    	String str = JSONObject.toJSONString(orig);
    	return JSONObject.parseObject(str, dest);

	}
}
