/*
 * @Date 2015-7-15
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 网络信息类
 */
package com.gcloud.header.compute.msg.node.vm.model;

import java.io.Serializable;

public class VmNetworkDetail implements Serializable{
	private String portId;
	private String portRefId;
	private Integer portProvider;
	private String sufId;
	private String brName;
	private String preName;
	private String aftName;
	private String macAddress;
	private String ip;
	private String subnetId;
	private String customOvsBr;
	private String deviceOwner;
	private Boolean noArpLimit;
	private String ovsBridgeId;
	
	public VmNetworkDetail(){
		
	}
	
	public String getPortId() {
		return portId;
	}
	public void setPortId(String portId) {
		this.portId = portId;
	}
	public String getBrName() {
		return brName;
	}
	public void setBrName(String brName) {
		this.brName = brName;
	}
	public String getPreName() {
		return preName;
	}
	public void setPreName(String preName) {
		this.preName = preName;
	}
	public String getAftName() {
		return aftName;
	}
	public void setAftName(String aftName) {
		this.aftName = aftName;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSubnetId() {
		return subnetId;
	}

	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}

	public String getCustomOvsBr() {
		return customOvsBr;
	}

	public void setCustomOvsBr(String customOvsBr) {
		this.customOvsBr = customOvsBr;
	}

	public String getDeviceOwner() {
		return deviceOwner;
	}

	public void setDeviceOwner(String deviceOwner) {
		this.deviceOwner = deviceOwner;
	}

	public String getSufId() {
		return sufId;
	}

	public void setSufId(String sufId) {
		this.sufId = sufId;
	}

    public Boolean getNoArpLimit() {
        return noArpLimit;
    }

    public void setNoArpLimit(Boolean noArpLimit) {
        this.noArpLimit = noArpLimit;
    }

	public String getPortRefId() {
		return portRefId;
	}

	public void setPortRefId(String portRefId) {
		this.portRefId = portRefId;
	}

	public Integer getPortProvider() {
		return portProvider;
	}

	public void setPortProvider(Integer portProvider) {
		this.portProvider = portProvider;
	}

	public String getOvsBridgeId() {
		return ovsBridgeId;
	}

	public void setOvsBridgeId(String ovsBridgeId) {
		this.ovsBridgeId = ovsBridgeId;
	}
}
