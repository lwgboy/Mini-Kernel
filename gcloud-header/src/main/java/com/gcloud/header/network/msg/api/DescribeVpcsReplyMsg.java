package com.gcloud.header.network.msg.api;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.network.model.DescribeVpcsResponse;
import com.gcloud.header.network.model.VpcsItemType;

public class DescribeVpcsReplyMsg extends PageReplyMessage<VpcsItemType> {
	
	@ApiModel(description = "vpc列表")
	private DescribeVpcsResponse vpcs;

	public DescribeVpcsResponse getVpcs() {
		return vpcs;
	}

	public void setVpcs(DescribeVpcsResponse vpcs) {
		this.vpcs = vpcs;
	}

	@Override
	public void setList(List<VpcsItemType> list) {
		// TODO Auto-generated method stub
		vpcs=new DescribeVpcsResponse();
		vpcs.setVpc(list);
	}
	
}
