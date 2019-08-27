package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

public class DescribeVRoutersResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<VRouterSetType> vRouter;

	public List<VRouterSetType> getvRouter() {
		return vRouter;
	}

	public void setvRouter(List<VRouterSetType> vRouter) {
		this.vRouter = vRouter;
	}
}
