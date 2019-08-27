package com.gcloud.header.compute.msg.api.region;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.compute.msg.api.model.DescribeRegionResponse;

public class ApiDescribeRegionReplyMsg extends ApiReplyMessage {

	private DescribeRegionResponse regions;

	public DescribeRegionResponse getRegions() {
		return regions;
	}

	public void setRegions(DescribeRegionResponse regions) {
		this.regions = regions;
	}
}
