package com.gcloud.header.identity.user;

import java.util.List;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.Module;

public class LoginMsg extends ApiMessage {
	private String loginName;
	private String password;
	private String verifyCode;
	private List<String> tests;

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return LoginReplyMsg.class;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public List<String> getTests() {
		return tests;
	}

	public void setTests(List<String> tests) {
		this.tests = tests;
	}
	
}
