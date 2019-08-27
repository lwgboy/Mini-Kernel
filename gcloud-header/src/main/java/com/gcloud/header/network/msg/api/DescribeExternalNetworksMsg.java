package com.gcloud.header.network.msg.api;

import java.util.List;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

public class DescribeExternalNetworksMsg extends ApiPageMessage{

	@ApiModel(description = "需要查询的Network的 Id列表")
	private List<String> networkIds;
	
	private Integer type;
	
	
	public List<String> getNetworkIds() {
		return networkIds;
	}


	public void setNetworkIds(List<String> networkIds) {
		this.networkIds = networkIds;
	}
	
	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return DescribeExternalNetworksReplyMsg.class;
	}

}
