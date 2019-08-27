package com.gcloud.header.compute.msg.api.vm.base;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.compute.msg.api.model.DescribeInstanceAttributesTypeResponse;
import com.gcloud.header.compute.msg.api.model.InstanceAttributesType;

public class ApiDescribeInstancesReplyMsg extends PageReplyMessage<InstanceAttributesType>{
	private DescribeInstanceAttributesTypeResponse instances;
	
	@Override
	public void setList(List<InstanceAttributesType> list) {
		instances = new DescribeInstanceAttributesTypeResponse();
		instances.setInstance(list);
	}

	public DescribeInstanceAttributesTypeResponse getInstances() {
		return instances;
	}

	public void setInstances(DescribeInstanceAttributesTypeResponse instances) {
		this.instances = instances;
	}
	
}
