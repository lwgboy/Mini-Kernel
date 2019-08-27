package com.gcloud.header.compute.msg.api.vm.base;

import java.util.List;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.ListReplyMessage;
import com.gcloud.header.compute.msg.api.model.DescribeInstanceTypesResponse;
import com.gcloud.header.compute.msg.api.model.InstanceTypeItemType;

public class ApiDescribeInstanceTypesReplyMsg extends ListReplyMessage<InstanceTypeItemType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DescribeInstanceTypesResponse instanceTypes;
	
	@Override
	public void setList(List<InstanceTypeItemType> list) {
		instanceTypes = new DescribeInstanceTypesResponse();
		instanceTypes.setInstanceType(list);
	}

	public DescribeInstanceTypesResponse getInstanceTypes() {
		return instanceTypes;
	}

	public void setInstanceTypes(DescribeInstanceTypesResponse instanceTypes) {
		this.instanceTypes = instanceTypes;
	}

}