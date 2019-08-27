package com.gcloud.header.compute.msg.node.node.model;

import java.io.Serializable;
import java.util.List;

public class DescribeNodesResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<NodeBaseInfo> node;

	public List<NodeBaseInfo> getNode() {
		return node;
	}

	public void setNode(List<NodeBaseInfo> node) {
		this.node = node;
	}
}
