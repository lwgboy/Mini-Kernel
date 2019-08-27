/*
 * @Date 2015-5-13
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
package com.gcloud.header.compute.msg.node.vm.model;

import java.io.Serializable;

public class VmCdromDetail implements Serializable{

	private String isoId;
	private String isoPath;
	private String isoPoolId; 
	private Integer isoStorageType;
	private String isoTargetDevice;
	public String getIsoId() {
		return isoId;
	}
	public void setIsoId(String isoId) {
		this.isoId = isoId;
	}
	public String getIsoPath() {
		return isoPath;
	}
	public void setIsoPath(String isoPath) {
		this.isoPath = isoPath;
	}
	public String getIsoPoolId() {
		return isoPoolId;
	}
	public void setIsoPoolId(String isoPoolId) {
		this.isoPoolId = isoPoolId;
	}
	public Integer getIsoStorageType() {
		return isoStorageType;
	}
	public void setIsoStorageType(Integer isoStorageType) {
		this.isoStorageType = isoStorageType;
	}
	public String getIsoTargetDevice() {
		return isoTargetDevice;
	}
	public void setIsoTargetDevice(String isoTargetDevice) {
		this.isoTargetDevice = isoTargetDevice;
	}
	public VmCdromDetail(String isoId, String isoPath, String isoPoolId,
			Integer isoStorageType, String isoTargetDevice) {
		this.isoId = isoId;
		this.isoPath = isoPath;
		this.isoPoolId = isoPoolId;
		this.isoStorageType = isoStorageType;
		this.isoTargetDevice = isoTargetDevice;
	}
	public VmCdromDetail() {
	}
	
}
