package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.api.ApiModel;

public class DescribeEipAddressesResponse implements Serializable{
	
	@ApiModel(description = "弹性公网IP列表")
	private List<EipAddressSetType> eipAddress;

	public List<EipAddressSetType> getEipAddress() {
		return eipAddress;
	}

	public void setEipAddress(List<EipAddressSetType> eipAddress) {
		this.eipAddress = eipAddress;
	}


}
