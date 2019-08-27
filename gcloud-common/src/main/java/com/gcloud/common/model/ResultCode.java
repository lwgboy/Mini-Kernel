/*
 * @Date 2015-4-13
 * 
 * @Author dengyf@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved. 
 * 
 * @Description 错误代码
 */
package com.gcloud.common.model;

public class ResultCode 
{
	private String code = "-1";
	private String message;
	
	public ResultCode(){}
	public ResultCode(String code, String msg)
	{
		this.code = code;
		this.message = msg;
	}
	public String getCode() 
	{
		return code;
	}
	public void setCode(String code) 
	{
		this.code = code;
	}
	public String getMessage() 
	{
		return message;
	}
	public void setMessage(String message) 
	{
		this.message = message;
	}
	
}
