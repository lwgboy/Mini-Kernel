package com.gcloud.common.util;

import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 访问http服务的客户端
 * 
 * @author Administrator
 * 
 */
public class HttpClientUtil {
	static Logger LOGGER = LogManager.getLogger(HttpClientUtil.class);
	static String charset="UTF-8";

	public static String doPost(String url, Map<String, Object> params) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String result = null;

		try {
			HttpPost httpPost = new HttpPost(url);
			if (params != null && params.size() > 0) {
				/*List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String value = entry.getValue().toString();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, charset);
				httpPost.setEntity(urlEncodedFormEntity);*/
				// 接收参数json列表  
                JSONObject jsonParam = new JSONObject(); 
                for (String s : params.keySet()) {
                	jsonParam.put(s, params.get(s));
    			}
                StringEntity stringEntity = new StringEntity(jsonParam.toString(),charset);//解决中文乱码问题    
                stringEntity.setContentEncoding(charset);    
                stringEntity.setContentType("application/json");    
                httpPost.setEntity(stringEntity);
			}
			httpClient = HttpClientBuilder.create().build();

			response = httpClient.execute(httpPost);

			entity = response.getEntity();
			if (entity != null) {
				int code=response.getStatusLine().getStatusCode();
				if(code==200){
					result = EntityUtils.toString(entity, charset);
				}
				else{
					LOGGER.error(EntityUtils.toString(entity, charset));
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}
		return result;
	}

	public static String doGet(String url, Map<String, Object> params) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String result = null;

		try {
			// 设置参数
			if (params != null && params.size() > 0) {
				StringBuffer sb = new StringBuffer();
				int i = 0;
				int len = params.size();
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					value=URLEncoder.encode(value, charset);
					if (i == len - 1) {
						sb.append(key + "=" + value);
					} else {
						sb.append(key + "=" + value);
						sb.append("&");
					}
					i++;
				}
				if (sb.length() > 0) {
					url += "?" + sb.toString();
				}
			}

			HttpGet httpGet = new HttpGet(url);
			httpClient = HttpClientBuilder.create().build();

			response = httpClient.execute(httpGet);

			entity = response.getEntity();
			if (entity != null) {
				int code=response.getStatusLine().getStatusCode();
				if(code==200){
					result = EntityUtils.toString(entity, charset);
				}
				else{
					LOGGER.error(EntityUtils.toString(entity, charset));
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}
		return result;
	}
	
	public static String doPatch(String url, Map<String, Object> params) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String result = null;

		try {
			HttpPatch httpPatch = new HttpPatch(url);
			httpPatch.setHeader("Content-type", "application/json");  
	        httpPatch.setHeader("Charset", charset);  
	        httpPatch.setHeader("Accept", "application/json");  
	        httpPatch.setHeader("Accept-Charset", charset);
			if (params != null && params.size() > 0) {
				// 接收参数json列表  
                JSONObject jsonParam = new JSONObject(); 
                for (String s : params.keySet()) {
                	jsonParam.put(s, params.get(s));
    			}
                StringEntity stringEntity = new StringEntity(jsonParam.toString(),charset);//解决中文乱码问题    
                stringEntity.setContentEncoding(charset);    
                stringEntity.setContentType("application/json");    
                httpPatch.setEntity(stringEntity);  
			}
			httpClient = HttpClientBuilder.create().build();

			response = httpClient.execute(httpPatch);

			entity = response.getEntity();
			if (entity != null) {
				int code=response.getStatusLine().getStatusCode();
				if(code==200){
					result = EntityUtils.toString(entity, charset);
				}
				else{
					LOGGER.error(EntityUtils.toString(entity, charset));
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		
	}
}
