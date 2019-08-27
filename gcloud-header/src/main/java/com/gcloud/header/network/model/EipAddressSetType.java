package com.gcloud.header.network.model;

import com.gcloud.header.api.ApiModel;
import com.gcloud.header.controller.ControllerProperty;

import java.io.Serializable;

public class EipAddressSetType implements Serializable{
	@ApiModel(description = "区域Id")
	private String regionId = ControllerProperty.REGION_ID;						//弹性公网IP所在的地域
	@ApiModel(description = "公网地址")
	private String ipAddress;						//弹性公网IP
	@ApiModel(description = "弹性公网IP实例Id")
	private String allocationId;					//弹性公网IP实例Id
	@ApiModel(description = "弹性公网IP当前的状态Available:可使用、InUse:已使用、Unassociating:解绑中、Associating:绑定中、Deleted:已删除")
	private String status;							//弹性公网IP当前的状态Available:可使用、InUse:已使用、Unassociating:解绑中、Associating:绑定中、Deleted:已删除
	@ApiModel(description = "绑定的实例id")
	private String instanceId;						//弹性公网IP当前绑定资源的Id；如果未绑定则值为空。
	@ApiModel(description = "弹性公网IP的公网带宽限速")
	private Integer	bandwidth;						//弹性公网IP的公网带宽限速，默认是5Mbps
	@ApiModel(description = "分配时间")
	private String allocationTime;					//分配时间。按照ISO8601标准表示，并需要使用UTC时间。格式为：YYYY-MM-DDThh:mmZ
	@ApiModel(description = "外网网络Id")
	private String externalNetworkId;
//	private String instanceName;
//	private String networkName;
	
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public Integer getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getAllocationTime() {
		return allocationTime;
	}
	public void setAllocationTime(String allocationTime) {
		this.allocationTime = allocationTime;
	}
	public String getExternalNetworkId() {
		return externalNetworkId;
	}
	public void setExternalNetworkId(String externalNetworkId) {
		this.externalNetworkId = externalNetworkId;
	}
//	public String getInstanceName() {
//		return instanceName;
//	}
//	public void setInstanceName(String instanceName) {
//		this.instanceName = instanceName;
//	}
//	public String getNetworkName() {
//		return networkName;
//	}
//	public void setNetworkName(String networkName) {
//		this.networkName = networkName;
//	}
}
