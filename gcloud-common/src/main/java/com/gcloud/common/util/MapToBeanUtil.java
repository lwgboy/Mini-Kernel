package com.gcloud.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

public class MapToBeanUtil {
	
	static{
	    ConvertUtils.register(new Converter()  
	           {  
	                  
	      
	               @SuppressWarnings({ "rawtypes", "unchecked" })  
	               public Object convert(Class arg0, Object arg1)  
	               {  
	                   System.out.println("注册字符串转换为date类型转换器");  
	                   if(arg1 == null)  
	                   {  
	                       return null;  
	                   }  
	                   if(!(arg1 instanceof String))  
	                   {  
	                       throw new ConversionException("只支持字符串转换 !");  
	                   }  
	                      
	                   String str = (String)arg1;  
	                   if(str.trim().equals(""))  
	                   {  
	                       return null;  
	                   }  
	                      
	                   SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	                      
	                   try{  
	                       return sd.parse(str);  
	                   }  
	                   catch(ParseException e)  
	                   {  
	                       throw new RuntimeException(e);  
	                   }  
	                      
	               }  
	                  
	           }, java.util.Date.class);  

	}
	
	 /**
     * 将一个 Map 对象转化为一个 JavaBean 使用时最好保证value值不为null 不支持枚举类型和 用户自定义类
     * @param type 要转化的javabean
     * @param map 包含属性值的map map的key值为javabean属性 key值小写
     * @return 转化出来的 JavaBean 对象
     * @throws Exception  Exception
     */
    public static Object convertMap(Class<?> type,String prefix, Map<String, Object> map)
            throws Exception
    {
        // 创建 JavaBean 对象
        Object obj = null;
 
        // 获取类属性
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        obj = type.newInstance();
        
        if(StringUtils.isNotBlank(prefix))
        {
        	prefix += ".";
        }

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName().substring(0, 1).toLowerCase(
                    Locale.getDefault()) + descriptor.getName().substring(1);

            if (map.containsKey(prefix + propertyName))
            {
            	if(map.get(prefix + propertyName) != null)
            	{
	                //String value = ConvertUtils.convert(map.get(prefix + propertyName));
            		String className = descriptor.getPropertyType().getSimpleName();
            		if(className.equals("List"))
	                {
    	                descriptor.getWriteMethod().invoke(obj, map.get(prefix + propertyName));
	                }
            		else// if(className.equals("String"))
            		{
            			Object[] args = new Object[1];
    	                args[0] = ConvertUtils.convert(ConvertUtils.convert(map.get(prefix + propertyName)), descriptor
    	                        .getPropertyType());
    	
    	                descriptor.getWriteMethod().invoke(obj, args);
            		}
	                
            	}

            }
        }
        return obj;
    }

}
