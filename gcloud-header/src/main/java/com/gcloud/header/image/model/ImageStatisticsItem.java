package com.gcloud.header.image.model;

import java.io.Serializable;

import com.gcloud.header.api.ApiModel;

public class ImageStatisticsItem  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description = "操作系统类型")
	private String osType;
	@ApiModel(description = "数量")
	private int countNum;
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	
	
}
