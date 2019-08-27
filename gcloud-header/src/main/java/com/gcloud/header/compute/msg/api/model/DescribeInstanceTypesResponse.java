package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;
import java.util.List;

/*
 * @Date Nov 7, 2018
 * 
 * @Author zhangzj
 * 
 * @Description TODO
 */
public class DescribeInstanceTypesResponse implements Serializable {

    private List<InstanceTypeItemType> instanceType;

	public List<InstanceTypeItemType> getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(List<InstanceTypeItemType> instanceType) {
		this.instanceType = instanceType;
	}
}
