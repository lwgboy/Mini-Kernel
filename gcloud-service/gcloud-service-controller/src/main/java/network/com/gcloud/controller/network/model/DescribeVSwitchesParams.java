package com.gcloud.controller.network.model;

import com.gcloud.common.model.PageParams;

public class DescribeVSwitchesParams extends PageParams {
	private String vpcId;
	private String vSwitchId;
	private String networkId;
	
	
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	public String getvSwitchId() {
		return vSwitchId;
	}
	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	
}
