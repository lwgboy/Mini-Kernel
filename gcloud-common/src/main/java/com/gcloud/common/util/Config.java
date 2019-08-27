package com.gcloud.common.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;



/**
 * @ClassName Config
 * @Description 读取配置文件
 * @author dengyf@gdeii.com.cn
 * @date 2012-6-21
 */
public class Config {
	protected Properties config = new Properties();
	/** 
	* @Fields configFileName : 配置文件名称
	*/
	protected String configFileName;

	/** 
	* <p>Title: </p> 
	* <p>Description:无参构造函数 </p>  
	*/
	public Config() {}

	/** 
	* <p>Title: </p> 
	* <p>Description: 加载文件名为fileName的文件</p> 
	* @param fileName
	* @throws Exception 
	*/
	public Config(String fileName) throws Exception {
		try {
			String path = getClass().getResource("/").getPath()+fileName;
			if(System.getProperty("os.name").toLowerCase().startsWith("win"))
				path = path.replaceAll("%20", " ").substring(1);
			FileInputStream fileInputStream = new FileInputStream(path);
			//config.load(fileInputStream);
			//中文编码问题
			InputStreamReader ir = new InputStreamReader(fileInputStream, "utf-8");
			config.load(ir);
			fileInputStream.close();
		} catch (IOException ex) {
			throw new Exception("无法读取指定的配置文件:" + fileName);
		}
		configFileName = fileName;
	}

	/** 
	* @Title: getValue 
	* @Description: 获取配置文件中itemName所对应的值
	* @param @param itemName
	* @param @return
	* @return String    返回类型 
	* @throws 
	*/
	public String getValue(String itemName) {
		return config.getProperty(itemName);
	}

	/** 
	* @Title: getValue 
	* @Description: 获取配置文件中itemName所对应的值，如果该属性不存在，则返回defaultValue的值
	* @param @param itemName
	* @param @param defaultValue
	* @param @return 
	* @return String    返回类型 
	* @throws 
	*/
	public String getValue(String itemName, String defaultValue) {
		return config.getProperty(itemName, defaultValue);
	}

	/** 
	* @Title: setValue 
	* @Description: 给配置文件中某字段赋值
	* @param @param itemName
	* @param @param value  
	* @return void    返回类型 
	* @throws 
	*/
	public void setValue(String itemName, String value) {
		config.setProperty(itemName, value);
		return;
	}

	/** 
	* @Title: saveFile 
	* @Description: 将内容保存到配置文件
	* @param @param fileName
	* @param @param description
	* @param @throws Exception 
	* @return void    返回类型 
	* @throws 
	*/
	public void saveFile(String fileName, String description) throws Exception {
		String path = getClass().getResource("/").getPath()+fileName;
		if(System.getProperty("os.name").toLowerCase().startsWith("win"))
			path = path.replaceAll("%20", " ").substring(1);
		FileOutputStream fileOutput = new FileOutputStream(path);
		try {
			config.store(fileOutput, description);// 保存文件
		} catch (IOException ex) {
			throw new Exception("无法保存指定的配置文件:" + fileName);
		}finally{
			fileOutput.close();
		}
	}

	/** 
	* @Title: saveFile 
	* @Description: 保存文件
	* @param @param fileName
	* @param @throws Exception
	* @return void    返回类型 
	* @throws 
	*/
	public void saveFile(String fileName) throws Exception {
		saveFile(fileName, "");
	}

	/** 
	* @Title: saveFile 
	* @Description: 保存文件
	* @param @throws Exception 
	* @return void    返回类型 
	* @throws 
	*/
	public void saveFile() throws Exception {
		if (configFileName.length() == 0)
			throw new Exception("需指定保存的配置文件名");
		saveFile(configFileName);
	}
}
