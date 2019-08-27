package com.gcloud.controller.compute.workflow.model.vm;


public class CreateInstanceByImageFlowCommandReq {
	private String instanceId;
	
	private String regionId;
	private String imageId;
	private String instanceType;
	private String instanceName;
	private String hostName;
	private String systemDiskName;
	private Integer systemDiskSize;
	private String systemDiskCategory;
	private String zoneId;
	
	private Integer cpu;
	private Integer memory;
	private String securityGroupId;
	
	private String curUserId;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getSystemDiskName() {
		return systemDiskName;
	}
	public void setSystemDiskName(String systemDiskName) {
		this.systemDiskName = systemDiskName;
	}
	public Integer getSystemDiskSize() {
		return systemDiskSize;
	}
	public void setSystemDiskSize(Integer systemDiskSize) {
		this.systemDiskSize = systemDiskSize;
	}
	public String getSystemDiskCategory() {
		return systemDiskCategory;
	}
	public void setSystemDiskCategory(String systemDiskCategory) {
		this.systemDiskCategory = systemDiskCategory;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getCurUserId() {
		return curUserId;
	}
	public void setCurUserId(String curUserId) {
		this.curUserId = curUserId;
	}
	public Integer getCpu() {
		return cpu;
	}
	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public String getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	
}
