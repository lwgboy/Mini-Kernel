package com.gcloud.header.compute.msg.node.node;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.compute.msg.node.node.model.DescribeNodesResponse;
import com.gcloud.header.compute.msg.node.node.model.NodeBaseInfo;

public class ApiDescribeNodesReplyMsg extends PageReplyMessage<NodeBaseInfo>{

	private static final long serialVersionUID = 1L;

	private DescribeNodesResponse nodes;

	@Override
	public void setList(List<NodeBaseInfo> list) {
		nodes = new DescribeNodesResponse();
		nodes.setNode(list);
	}

	public DescribeNodesResponse getNodes() {
		return nodes;
	}

	public void setNodes(DescribeNodesResponse nodes) {
		this.nodes = nodes;
	}
}
