package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.GcloudConstants;
import com.gcloud.header.api.ApiModel;

public class VSwitchSetType implements Serializable{
	@ApiModel(description = "交换机ID")
	@TableField("id")
	private String vSwitchId;
	@ApiModel(description = "网络ID")
	@TableField("network_id")
	private String networkId;
	@ApiModel(description = "交换机所在的专有网络ID")
	@TableField("router_id")
	private String vpcId;
	@ApiModel(description = "交换机的地址")
	@TableField("cidr")
	private String cidrBlock;
	@ApiModel(description = "交换机名字")
	@TableField("name")
	private String vSwitchName;
	@ApiModel(description = "可用区ID")
	@TableField("zone_id")
	private String zoneId;
	@ApiModel(description = "关联的路由ID")
	@TableField("router_id")
	private String vRouterId;
	@ApiModel(description = "创建时间")
	@TableField("create_time")
	@JsonFormat(timezone = GcloudConstants.DEFAULT_TIMEZONE, pattern = GcloudConstants.DEFAULT_DATEFORMAT)
	private Date creationTime;
	
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
	public String getCidrBlock() {
		return cidrBlock;
	}
	public void setCidrBlock(String cidrBlock) {
		this.cidrBlock = cidrBlock;
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
	public String getvRouterId() {
		return vRouterId;
	}
	public void setvRouterId(String vRouterId) {
		this.vRouterId = vRouterId;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	
}
