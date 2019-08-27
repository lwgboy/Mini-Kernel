package com.gcloud.header.storage.model;

import java.io.Serializable;

public class DiskStatusStatisticsItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private int countNum;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	
}
