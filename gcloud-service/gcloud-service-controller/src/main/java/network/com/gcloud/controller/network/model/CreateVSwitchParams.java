package com.gcloud.controller.network.model;

public class CreateVSwitchParams {
	private String cidrBlock;
	//vpcId字段，若不传必须为空字符串
	private String vpcId;
	private String vSwitchName;
	//networkId，若不传必须为空字符串
	private String networkId;
	private String zoneId;
	
	//private String curUserId;
	private String regionId;
	public String getCidrBlock() {
		return cidrBlock;
	}
	public void setCidrBlock(String cidrBlock) {
		this.cidrBlock = cidrBlock;
	}
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	public String getvSwitchName() {
		return vSwitchName;
	}
	public void setvSwitchName(String vSwitchName) {
		this.vSwitchName = vSwitchName;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	
}
