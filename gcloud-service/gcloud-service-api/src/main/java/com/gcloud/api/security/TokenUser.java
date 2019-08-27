package com.gcloud.api.security;

import java.io.Serializable;

public class TokenUser implements Serializable{
	private String userId;
	private Long expressTime;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getExpressTime() {
		return expressTime;
	}
	public void setExpressTime(Long expressTime) {
		this.expressTime = expressTime;
	}
}
