package com.gcloud.api.security;

import java.io.Serializable;

public class SignUser implements Serializable{
	private String secretKey;
	private String userId;
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
