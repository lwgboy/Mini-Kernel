package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeVServerGroupsMsg extends ApiMessage {
	
	@ApiModel(description = "负载均衡ID", require = true)
	@NotBlank(message = "0140301::负载均衡器ID不能为空")
	private String loadBalancerId;
	
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiDescribeVServerGroupsReplyMsg.class;
	}
	public String getLoadBalancerId() {
		return loadBalancerId;
	}
	public void setLoadBalancerId(String loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}

}
