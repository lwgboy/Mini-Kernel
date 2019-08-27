package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.api.ApiModel;


public class DescribeExternalNetworksResponse implements Serializable{

	@ApiModel(description = "外部网络列表")
	private List<ExternalNetworkSetType> network;

	public List<ExternalNetworkSetType> getNetwork() {
		return network;
	}

	public void setNetwork(List<ExternalNetworkSetType> network) {
		this.network = network;
	}
	
	
	
	
}
