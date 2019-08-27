package com.gcloud.header.network.msg.api;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.network.model.DescribeNetworkInterfacesResponse;
import com.gcloud.header.network.model.NetworkInterfaceSet;

public class DescribeNetworkInterfacesReplyMsg extends PageReplyMessage<NetworkInterfaceSet> {

	private static final long serialVersionUID = 1L;

	private DescribeNetworkInterfacesResponse networkInterfaceSets;
	@Override
	public void setList(List<NetworkInterfaceSet> list) {
		networkInterfaceSets = new DescribeNetworkInterfacesResponse();
		networkInterfaceSets.setNetworkInterfaceSet(list);
		
	}
	public DescribeNetworkInterfacesResponse getNetworkInterfaceSets() {
		return networkInterfaceSets;
	}
	public void setNetworkInterfaceSets(DescribeNetworkInterfacesResponse networkInterfaceSets) {
		this.networkInterfaceSets = networkInterfaceSets;
	}
	
}
