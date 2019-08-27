package com.gcloud.header.log.enums;

public enum LogStatus {

	COMPLETE("COMPLETE"), FAILED("FAILED"), IN_PROGRESS("IN_PROGRESS"), TIMEOUT("TIMEOUT");

	LogStatus(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

}
