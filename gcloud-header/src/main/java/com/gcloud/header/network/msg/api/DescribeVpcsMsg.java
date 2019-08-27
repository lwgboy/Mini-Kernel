package com.gcloud.header.network.msg.api;

import java.util.List;

import javax.validation.constraints.Size;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

public class DescribeVpcsMsg extends ApiPageMessage{
	@ApiModel(description="专有网络ID列表")
	@Size(max=20, message="0100201::列表size不能超过20")
	private List<String> vpcIds;
	
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return DescribeVpcsReplyMsg.class;
	}


	public List<String> getVpcIds() {
		return vpcIds;
	}


	public void setVpcIds(List<String> vpcIds) {
		this.vpcIds = vpcIds;
	}
	
}
