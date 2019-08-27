/*
 * @Date 2015-12-16
 * 
 * @Author dengyf@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved. 
 * 
 * @Description 对参数签名（admin用户）
 */
package com.gcloud.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gcloud.common.crypto.Hmac;
import com.gcloud.common.crypto.HmacAuth;

public class SignParamsUtil {
	private static final Logger log = LoggerFactory.getLogger(SignParamsUtil.class);
	//private static final String SECRET_KEY = "O3FY24GpnN4gAh7lcsizvhmioCwf3g5EwK5TR4aJ";
	private static TimeZone serverTimeZone = TimeZone.getTimeZone("GMT");
	
	public static Map<String, Object> signParams(Map<String, Object> params,String accessKeyId, String secretkey)
	{
		try
        {
			params.put("AWSAccessKeyId", accessKeyId);
			params.put("SignatureVersion", "1");
			params.put("Timestamp", DateTimeUtil.httpDate(serverTimeZone));
	    	String macData = HmacAuth.makeSubjectString(params, null);
	        String sign = HmacAuth.getSignature(secretkey, macData, Hmac.HmacSHA1);
	        params.put("Signature", sign.replace("\r\n",""));
	    } 
	    catch (UnsupportedEncodingException e)
	    {
	        
	    	log.error("参数签名失败，params:" + params + e.getMessage());
	    } 
		return params;
	}
}
