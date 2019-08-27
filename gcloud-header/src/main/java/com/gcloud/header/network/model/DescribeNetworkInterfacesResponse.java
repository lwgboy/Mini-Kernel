package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

public class DescribeNetworkInterfacesResponse  implements Serializable{
	private List<NetworkInterfaceSet> networkInterfaceSet;

	public List<NetworkInterfaceSet> getNetworkInterfaceSet() {
		return networkInterfaceSet;
	}

	public void setNetworkInterfaceSet(List<NetworkInterfaceSet> networkInterfaceSet) {
		this.networkInterfaceSet = networkInterfaceSet;
	}
}
