package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

public class DescribeVpcsResponse implements Serializable{
	private List<VpcsItemType> vpc;

	public List<VpcsItemType> getVpc() {
		return vpc;
	}

	public void setVpc(List<VpcsItemType> vpc) {
		this.vpc = vpc;
	}

}
