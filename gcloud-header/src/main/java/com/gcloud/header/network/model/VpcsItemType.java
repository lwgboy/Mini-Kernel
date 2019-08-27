
package com.gcloud.header.network.model;


import com.gcloud.framework.db.jdbc.annotation.Table;
import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.controller.ControllerProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Table(name="gc_network")
public class VpcsItemType implements Serializable{
	
	@ApiModel(description = "区域ID")
	@TableField("region_id")
	private String regionId = ControllerProperty.REGION_ID;
	@ApiModel(description = "状态")
	private String status;
	@ApiModel(description = "ID")
	@TableField("id")
	private String vpcId;
	private String vpcUuid;
	@TableField("name")
	private String vpcName;
	private String cnStatus;
//	private String cidrBlock;    
	//网络的网络段
	@ApiModel(description = "子网ID列表")
	private Map<String, List> vSwitchIds;
	
	public String getVpcUuid() {
		return vpcUuid;
	}
	public void setVpcUuid(String vpcUuid) {
		this.vpcUuid = vpcUuid;
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
		//this.cnStatus = EcsNetworkStatus.getEcsCnStatus(status);
	}

//	public String getCidrBlock() {
//		return cidrBlock;
//	}
//	public void setCidrBlock(String cidrBlock) {
//		this.cidrBlock = cidrBlock;
//	}
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	public String getVpcName() {
		return vpcName;
	}
	public void setVpcName(String vpcName) {
		this.vpcName = vpcName;
	}
	public String getCnStatus() {
		return cnStatus;
	}
	public void setCnStatus(String cnStatus) {
		this.cnStatus = cnStatus;
	}
	public Map<String, List> getvSwitchIds() {
		return vSwitchIds;
	}
	public void setvSwitchIds(Map<String, List> vSwitchIds) {
		this.vSwitchIds = vSwitchIds;
	}




}
