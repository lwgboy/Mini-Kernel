package com.gcloud.header.slb.msg.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.gcloud.header.ApiMessage;

public class ApiDescribeLoadBalancersMsg extends ApiMessage {

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiDescribeLoadBalancersReplyMsg.class;
	}
	
	
}
