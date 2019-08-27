package com.gcloud.header.network.msg.api;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.network.model.DescribeEipAddressesResponse;
import com.gcloud.header.network.model.EipAddressSetType;

public class DescribeEipAddressesReplyMsg  extends PageReplyMessage<EipAddressSetType> {
	
	@ApiModel(description = "弹性公网IP信息")
	private DescribeEipAddressesResponse eipAddresses;
	@Override
	public void setList(List<EipAddressSetType> list) {
		eipAddresses = new DescribeEipAddressesResponse();
		eipAddresses.setEipAddress(list);
	}
	public DescribeEipAddressesResponse getEipAddresses() {
		return eipAddresses;
	}
	public void setEipAddresses(DescribeEipAddressesResponse eipAddresses) {
		this.eipAddresses = eipAddresses;
	}
	
}
