package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

import javax.validation.constraints.NotBlank;

public class CreateVSwitchMsg extends ApiMessage {
	@NotBlank(message = "0030101::交换机网段不能为空")
	@ApiModel(description = "网段，形如1.1.1.1/20", require = true)
	private String cidrBlock;
	@ApiModel(description = "指定VSwitch所在的 VPC")
	private String vpcId;
	@ApiModel(description = "非VPC模式网络ID")
	private String networkId;
	@NotBlank(message = "0030102::名称不能为空")
	@ApiModel(description = "交换机名称", require = true)
	private String vSwitchName;
	@ApiModel(description = "VSwitch所属区的ID")
	@NotBlank(message = "0030103::可用区不能为空")
	private String zoneId;
	@ApiModel(description = "网关IP")
	private String gateWayIp;
	@ApiModel(description = "区域ID")
	private String regionId;
	
	@Override
	public Class replyClazz() {
		return CreateVSwitchReplyMsg.class;
	}
	
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
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getGateWayIp() {
		return gateWayIp;
	}

	public void setGateWayIp(String gateWayIp) {
		this.gateWayIp = gateWayIp;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
}
