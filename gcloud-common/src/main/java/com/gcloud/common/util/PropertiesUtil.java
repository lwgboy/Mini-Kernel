package com.gcloud.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * @Date 2015-4-1
 * 
 * @Author yaowj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 读取配置文件
 */
public class PropertiesUtil {

	public static Properties loadProperty(String propertyFile) {
		
		Properties prop = new Properties();
		InputStream is = null;
		
		try {
			
			is = new FileInputStream(propertyFile);
			prop.load(is);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} finally {

			if(is != null){
				try {
					is.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}

		return prop;
	}

}
