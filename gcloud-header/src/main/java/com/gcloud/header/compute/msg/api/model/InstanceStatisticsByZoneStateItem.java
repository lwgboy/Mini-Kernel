package com.gcloud.header.compute.msg.api.model;

public class InstanceStatisticsByZoneStateItem {
	private String zoneId;
	private String state;
	private int countNum;
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
