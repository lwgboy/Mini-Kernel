package com.gcloud.service.common.compute.model;

public class DomainDisk {
	
	private String diskDevice;
	private String diskType;
	private String driverName;
	private String driverCache;
	private String driverType;
	private String sourceFile;
	private String sourceDev;
	private String targetDev;
	private String targetBus;
	
	public String getDiskDevice() {
		return diskDevice;
	}
	public void setDiskDevice(String diskDevice) {
		this.diskDevice = diskDevice;
	}
	public String getDiskType() {
		return diskType;
	}
	public void setDiskType(String diskType) {
		this.diskType = diskType;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverCache() {
		return driverCache;
	}
	public void setDriverCache(String driverCache) {
		this.driverCache = driverCache;
	}
	public String getDriverType() {
		return driverType;
	}
	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}
	public String getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	public String getTargetDev() {
		return targetDev;
	}
	public void setTargetDev(String targetDev) {
		this.targetDev = targetDev;
	}
	public String getTargetBus() {
		return targetBus;
	}
	public void setTargetBus(String targetBus) {
		this.targetBus = targetBus;
	}
	public String getSourceDev() {
		return sourceDev;
	}
	public void setSourceDev(String sourceDev) {
		this.sourceDev = sourceDev;
	}
	
	

}
