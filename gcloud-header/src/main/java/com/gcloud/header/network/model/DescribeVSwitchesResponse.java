package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.api.ApiModel;

public class DescribeVSwitchesResponse implements Serializable{
	
	@ApiModel(description = "子网列表")
	private List<VSwitchSetType> vSwitch;

	public List<VSwitchSetType> getvSwitch() {
		return vSwitch;
	}

	public void setvSwitch(List<VSwitchSetType> vSwitch) {
		this.vSwitch = vSwitch;
	}
	
}
