/*
 * @Date 2015-5-13
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
package com.gcloud.header.storage.model;

import java.io.Serializable;

public class VmVolumeDetail implements Serializable {

	private String fileFormat;// qcow2 ,vhd,aio,phy
	private String sourcePath;//
	private String targetDev;// vda
	private String busType;// virtio
	private Integer volumeSize;// 块设备大小（MB）
	private String volumeId;// 卷Id
    private String volumeRefId;
	private String diskType;// 系统盘，数据盘
	private String category;
	private Integer virtualSize;// qcow2文件大小（MB）
	private String protocol;
	private String storageType;
	private Integer provider;
	private String storageDriver;

	public VmVolumeDetail() {

	}

	public VmVolumeDetail(Integer volumeSize, Integer virtualSize, String fileFormat, String sourcePath, String targetDev, String busType, String diskType, String volumeId, String category, String volumeRefId, Integer provider, String protocol, String storageDriver) {
		this.fileFormat = fileFormat;
		this.sourcePath = sourcePath;
		this.targetDev = targetDev;
		this.busType = busType;
		this.volumeSize = volumeSize;
		this.diskType = diskType;
		this.volumeId = volumeId;
		this.category = category;
		this.virtualSize = virtualSize;
		this.volumeRefId = volumeRefId;
		this.provider = provider;
		this.protocol = protocol;
		this.storageDriver = storageDriver;
	}

	public Integer getVolumeSize() {
		return volumeSize;
	}

	public void setVolumeSize(Integer volumeSize) {
		this.volumeSize = volumeSize;
	}

	public String getDiskType() {
		return diskType;
	}

	public void setDiskType(String diskType) {
		this.diskType = diskType;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getTargetDev() {
		return targetDev;
	}

	public void setTargetDev(String targetDev) {
		this.targetDev = targetDev;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getVirtualSize() {
		return virtualSize;
	}

	public void setVirtualSize(Integer virtualSize) {
		this.virtualSize = virtualSize;
	}


	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getVolumeRefId() {
        return volumeRefId;
    }

    public void setVolumeRefId(String volumeRefId) {
        this.volumeRefId = volumeRefId;
    }

	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}

	public String getStorageDriver() {
		return storageDriver;
	}

	public void setStorageDriver(String storageDriver) {
		this.storageDriver = storageDriver;
	}
}
