package com.gcloud.header.slb.msg.api;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.slb.model.DescribeLoadBalancersResponse;
import com.gcloud.header.slb.model.LoadBalancerModel;

public class ApiDescribeLoadBalancersReplyMsg extends PageReplyMessage<LoadBalancerModel> {
	
	private static final long serialVersionUID = 1L;

	@ApiModel(description = "负载均衡信息")
	private DescribeLoadBalancersResponse loadBalancers;
	
	@Override
	public void setList(List<LoadBalancerModel> list) {
		// TODO Auto-generated method stub
		loadBalancers = new DescribeLoadBalancersResponse();
	    loadBalancers.setLoadBalancers(list);
	}

	public DescribeLoadBalancersResponse getLoadBalancers() {
		return loadBalancers;
	}

	public void setLoadBalancers(DescribeLoadBalancersResponse loadBalancers) {
		this.loadBalancers = loadBalancers;
	}
	
}
