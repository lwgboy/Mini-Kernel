package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.GcloudConstants;
import com.gcloud.header.api.ApiModel;



public class ExternalNetworkSetType implements Serializable{

	@ApiModel(description = "创建时间")
	@TableField("create_time")
	@JsonFormat(timezone = GcloudConstants.DEFAULT_TIMEZONE, pattern = GcloudConstants.DEFAULT_DATEFORMAT)
	private Date creationTime;
	@ApiModel(description = "区域ID")
	@TableField("region_id")
	private String regionId;
	@ApiModel(description = "状态")
	@TableField("status")
	private String status;
	private VSwitchIds vSwitchIds;
	private String subnetIds;
	@ApiModel(description = "ID")
	@TableField("id")
	private String networkId;
	@ApiModel(description = "创建时间")
	@TableField("name")
	private String networkName;
	@ApiModel(description = "网络类型,取值 0内部网络|1外部网络")
	@TableField("type")
	private Integer type;
	private String cnStatus;
//	private String cidrBlock;
	
	
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public VSwitchIds getvSwitchIds() {
		return vSwitchIds;
	}
	public void setvSwitchIds(VSwitchIds vSwitchIds) {
		this.vSwitchIds = vSwitchIds;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public String getCnStatus() {
		return cnStatus;
	}
	public void setCnStatus(String cnStatus) {
		this.cnStatus = cnStatus;
	}
//	public String getCidrBlock() {
//		return cidrBlock;
//	}
//	public void setCidrBlock(String cidrBlock) {
//		this.cidrBlock = cidrBlock;
//	}
	public String getSubnetIds() {
		return subnetIds;
	}
	public void setSubnetIds(String subnetIds) {
		this.subnetIds = subnetIds;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	} 

	
	
}
