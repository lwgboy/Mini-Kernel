package com.gcloud.controller.monitor.model;

import java.io.Serializable;

public class StatisticsPoint implements Serializable{
	private String time;
	private Double value;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
}
