package com.gcloud.api;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcloud.identity.user")
public class ApiIdentityConfig {

	@Value("${gcloud.identity.user.tokenTimeout:300}")
    private int tokenTimeout;//单位s
	
	
	@Value("${gcloud.identity.user.verifyCode:false}")
    private boolean verifyCode;
	
	@Value("${gcloud.identity.api.check.signatureCheck:false}")
    private boolean signatureCheck;
	
	@Value("${gcloud.identity.api.check.timeout:5}")
    private int apiTimeout;

	public int getTokenTimeout() {
		return tokenTimeout;
	}

	public void setTokenTimeout(int tokenTimeout) {
		this.tokenTimeout = tokenTimeout;
	}

	public boolean isVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(boolean verifyCode) {
		this.verifyCode = verifyCode;
	}

	public boolean isSignatureCheck() {
		return signatureCheck;
	}

	public void setSignatureCheck(boolean signatureCheck) {
		this.signatureCheck = signatureCheck;
	}

	public int getApiTimeout() {
		return apiTimeout;
	}

	public void setApiTimeout(int apiTimeout) {
		this.apiTimeout = apiTimeout;
	}
}