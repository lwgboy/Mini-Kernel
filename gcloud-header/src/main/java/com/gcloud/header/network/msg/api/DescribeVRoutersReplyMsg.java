package com.gcloud.header.network.msg.api;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.network.model.DescribeVRoutersResponse;
import com.gcloud.header.network.model.VRouterSetType;

public class DescribeVRoutersReplyMsg  extends PageReplyMessage<VRouterSetType> {

	private static final long serialVersionUID = 1L;
	
	private DescribeVRoutersResponse vRouters;
	
	@Override
	public void setList(List<VRouterSetType> list) {
		vRouters = new DescribeVRoutersResponse();
		vRouters.setvRouter(list);
	}

	public DescribeVRoutersResponse getvRouters() {
		return vRouters;
	}

	public void setvRouters(DescribeVRoutersResponse vRouters) {
		this.vRouters = vRouters;
	}
}
