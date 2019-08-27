package com.gcloud.header.log.model;

import java.io.Serializable;
import java.util.List;

public class DescribeLogAttributesTypeResponse implements Serializable{
	private List<LogAttributesType> log;

	public List<LogAttributesType> getLog() {
		return log;
	}

	public void setLog(List<LogAttributesType> log) {
		this.log = log;
	}

}
