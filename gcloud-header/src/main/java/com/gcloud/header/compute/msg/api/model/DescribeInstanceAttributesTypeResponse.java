package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;
import java.util.List;

public class DescribeInstanceAttributesTypeResponse implements Serializable{
	private List<InstanceAttributesType> instance;

	public List<InstanceAttributesType> getInstance() {
		return instance;
	}

	public void setInstance(List<InstanceAttributesType> instance) {
		this.instance = instance;
	}

}
