package com.gcloud.core.log.enums;

public enum LogResult {
	SUCCESS((byte)1, "成功"),
	FAIL((byte)2, "失败"),
	RUNNING((byte)3, "进行中"),
	TIMEOUT((byte)4, "超时");
	
	private Byte result;
	private String resultCn;
	LogResult(Byte result, String resultCn)
	{
		this.result = result;
		this.resultCn = resultCn;
	}
	public Byte getResult() {
		return result;
	}
	public void setResult(Byte result) {
		this.result = result;
	}
	public String getResultCn() {
		return resultCn;
	}
	public void setResultCn(String resultCn) {
		this.resultCn = resultCn;
	}
	
}
