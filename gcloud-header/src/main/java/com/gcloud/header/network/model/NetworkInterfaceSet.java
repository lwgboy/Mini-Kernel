package com.gcloud.header.network.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.api.ApiModel;

import java.io.Serializable;

public class NetworkInterfaceSet  implements Serializable{
	
	@ApiModel(description = "网卡ID")
	@TableField("id")
	private String networkInterfaceId; // 弹性网卡的 ID
	@ApiModel(description = "网卡名称")
	@TableField("name")
	private String networkInterfaceName; // 弹性网卡的名称
	@ApiModel(description = "创建时间")
	@TableField("create_time")
	private String creationTime;
	@ApiModel(description = "安全组ID")
	private SecurityGroupIdSetType securityGroupIds;
	@ApiModel(description = "子网ID")
	@TableField("subnet_id")
	private String vSwitchId;
	@ApiModel(description = "网络ID")
	@TableField("router_id")
	private String vpcId;
	@ApiModel(description = "mac地址")
	private String macAddress; // 弹性网卡的 MAC 地址
	@ApiModel(description = "私有IP地址")
	@TableField("ip_address")
	private String privateIpAddress; // 弹性网卡主私有 IP 地址
	@ApiModel(description = "状态")
	private String status; // 弹性网卡的状态
	@ApiModel(description = "实例ID")
	@TableField("device_id")
	private String deviceId; // 弹性网卡当前关联的实例 ID
	@ApiModel(description = "实例类型")
	@TableField("device_owner")
	private String deviceType; // 弹性网卡当前关联的实例 ID

	@JsonIgnore
	private String securityGroupIdsStr;

	public String getNetworkInterfaceId() {
		return networkInterfaceId;
	}

	public void setNetworkInterfaceId(String networkInterfaceId) {
		this.networkInterfaceId = networkInterfaceId;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getNetworkInterfaceName() {
		return networkInterfaceName;
	}

	public void setNetworkInterfaceName(String networkInterfaceName) {
		this.networkInterfaceName = networkInterfaceName;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}


	public String getvSwitchId() {
		return vSwitchId;
	}

	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public SecurityGroupIdSetType getSecurityGroupIds() {
		return securityGroupIds;
	}

	public void setSecurityGroupIds(SecurityGroupIdSetType securityGroupIds) {
		this.securityGroupIds = securityGroupIds;
	}

	public String getSecurityGroupIdsStr() {
		return securityGroupIdsStr;
	}

	public void setSecurityGroupIdsStr(String securityGroupIdsStr) {
		this.securityGroupIdsStr = securityGroupIdsStr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
