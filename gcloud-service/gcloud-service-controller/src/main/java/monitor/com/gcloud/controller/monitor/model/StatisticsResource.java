package com.gcloud.controller.monitor.model;

import java.io.Serializable;
import java.util.List;

public class StatisticsResource implements Serializable{
	private String resourceId;
	private String host;
	private String instance;
	private List<StatisticsPoint> points;
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public List<StatisticsPoint> getPoints() {
		return points;
	}
	public void setPoints(List<StatisticsPoint> points) {
		this.points = points;
	}
}
