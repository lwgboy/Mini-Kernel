package com.gcloud.service.common.compute.uitls;


import com.gcloud.core.exception.GCloudException;
import lombok.extern.slf4j.Slf4j;

import javax.xml.parsers.FactoryConfigurationError;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class LogUtil {
//	private static Logger LOG = LogManager.getLogger(LogUtil.class);
	
	public static void handleLog(String[] cmd, int res, String errorCode) throws GCloudException {
		if (res != 0) {
			String commandString = "";
			for (String part : cmd) {
				commandString += part + " ";
			}
			
			Throwable ex = new Throwable();
	        /*StackTraceElement[] stackElements = ex.getStackTrace();
	        StringBuffer sb = new StringBuffer();
	        if (stackElements != null) {
	            for (int i = 0; i < stackElements.length; i++) {
	            	sb.append(stackElements[i].getClassName()+" ");
	            	sb.append(stackElements[i].getFileName()+" ");
	            	sb.append(stackElements[i].getLineNumber()+" ");
	            	sb.append(stackElements[i].getMethodName() + "\n");
	            }
	        }*/
			log.error(commandString, ex);
			throw new GCloudException(errorCode);
		}
	}
	
	public static void logInit(Map<String, String> propertys) throws FactoryConfigurationError {
		System.setProperty("log4j_gcloud_debug", propertys.get("log4j_gcloud_debug"));
		System.setProperty("log4j_debug", propertys.get("log4j_debug"));
		System.setProperty("log4j_info", propertys.get("log4j_info"));
		System.setProperty("log4j_error", propertys.get("log4j_error"));
	}
	
	public static void debugInit(Map<String, String> propertys) {
		propertys.put("log4j_gcloud_debug", "ALL");
		propertys.put("log4j_debug", "DEBUG");
		propertys.put("log4j_info", "INFO");
		propertys.put("log4j_error", "ERROR");
	}

	public static void releaseInit(Map<String, String> propertys) {
		propertys.put("log4j_gcloud_debug", "OFF");
		propertys.put("log4j_debug", "OFF");
		propertys.put("log4j_info", "OFF");
		propertys.put("log4j_error", "ERROR");
	}
	
	/**
	 * 
	  * @Title: getProperty
	  * @Description: 读取gcloud-clc.cfg配置文件中的log.properties日志级别属性
	  * @date 2015-7-6 上午9:56:48
	  *
	  * @param logProp  日志级别
	  * @param configPath    配置文件路径
	  * @return
	 */
	public static Map<String, String> getProperty(String logProp, String configPath) {
		Map<String, String> propertys = new HashMap<String, String>();
		String filePath = configPath;
		File f = new File(filePath);
		String log4j;
		if (f.exists()) {
			try {
				log4j = logProp;
				if (log4j != null && log4j.trim().equals("debug")) // 开发环境
				{
					LogUtil.debugInit(propertys);
				} else // 生产环境
				{
					LogUtil.releaseInit(propertys);
				}
			} catch (Exception e) {// 生产环境
				LogUtil.releaseInit(propertys);
			}
		} else {// 生产环境
			LogUtil.releaseInit(propertys);
		}
		return propertys;
	}
}
