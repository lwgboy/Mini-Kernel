package com.gcloud.common.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {
	public static void post(String urlStr){
		URL url;
		try {
			url = new URL(urlStr);
			url.openConnection();
		} catch (MalformedURLException e) {
			
			System.out.println(e.getMessage());
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
		}
	}
	
	/** 
	* @Title testConnection 
	* @Description 测试url联通性
	* @param @param urlStr
	* @param @return 
	* @return boolean    返回类型 
	* @throws 
	*/
	public static boolean testConnection(String urlStr)
	{
		try
		{  
            URL url = new URL(urlStr);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            /** 
             * public int getResponseCode()throws IOException 
             * 从 HTTP 响应消息获取状态码。 
             * 例如，就以下状态行来说：  
             * HTTP/1.0 200 OK 
             * HTTP/1.0 401 Unauthorized 
             * 将分别返回 200 和 401。 
             * 如果无法从响应中识别任何代码（即响应不是有效的 HTTP），则返回 -1。  
             *  
             * 返回 HTTP 状态码或 -1 
             */  
            int state = conn.getResponseCode();  
            if(state == 200)
            {  
            	return true;
            }  
            else
            {  
            	return false;
            }  
        }  
        catch(IOException e)
        {  
        	return false;
        }  
	}
}
