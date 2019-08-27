package com.gcloud.header.network.msg.api;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.network.model.DescribeExternalNetworksResponse;
import com.gcloud.header.network.model.ExternalNetworkSetType;

public class DescribeExternalNetworksReplyMsg extends PageReplyMessage<ExternalNetworkSetType> {
	
	@ApiModel(description = "外部网络信息")
	private DescribeExternalNetworksResponse networks;
	
	@Override
	public void setList(List<ExternalNetworkSetType> list) {
		// TODO Auto-generated method stub
		networks = new DescribeExternalNetworksResponse();
		networks.setNetwork(list);
	}

	public DescribeExternalNetworksResponse getNetworks() {
		return networks;
	}

	public void setNetworks(DescribeExternalNetworksResponse networks) {
		this.networks = networks;
	}
	
	

}
