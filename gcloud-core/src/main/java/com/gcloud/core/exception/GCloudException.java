/*
 * @Date 2015-4-1
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description TODO
 */
package com.gcloud.core.exception;


public class GCloudException extends RuntimeException {

	private String[] params;

	public GCloudException(){}
	
	public GCloudException(String msg) {
		super(msg);
	}

	public GCloudException(Exception e) {
		super(e);
	}

	public GCloudException(String code, String... params){
		
		super(code);
		this.setParams(params);
		
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

}
