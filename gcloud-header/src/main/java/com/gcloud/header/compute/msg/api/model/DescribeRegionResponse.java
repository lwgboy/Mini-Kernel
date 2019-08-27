package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;
import java.util.List;

public class DescribeRegionResponse implements Serializable{
	private List<RegionType> region;

	public List<RegionType> getRegion() {
		return region;
	}

	public void setRegion(List<RegionType> region) {
		this.region = region;
	}
}
