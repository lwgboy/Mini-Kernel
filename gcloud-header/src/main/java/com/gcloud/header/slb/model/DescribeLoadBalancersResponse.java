package com.gcloud.header.slb.model;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.api.ApiModel;
import com.gcloud.header.storage.model.DiskItemType;

public class DescribeLoadBalancersResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModel(description = "负载均衡列表")
    private List<LoadBalancerModel> loadBalancers;

	public List<LoadBalancerModel> getLoadBalancers() {
		return loadBalancers;
	}

	public void setLoadBalancers(List<LoadBalancerModel> loadBalancers) {
		this.loadBalancers = loadBalancers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
