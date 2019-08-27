package com.gcloud.controller.compute.handler.api.node;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.node.DescribeNodesParams;
import com.gcloud.controller.compute.service.node.IComputeNodeService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.node.node.ApiDescribeNodesMsg;
import com.gcloud.header.compute.msg.node.node.ApiDescribeNodesReplyMsg;
import com.gcloud.header.compute.msg.node.node.model.NodeBaseInfo;

@ApiHandler(module = Module.ECS, subModule= SubModule.NODE, action = "DescribeNodes")
@GcLog(taskExpect = "节点列表")
public class ApiDescribeNodesHandler extends MessageHandler<ApiDescribeNodesMsg, ApiDescribeNodesReplyMsg>{
	
	@Autowired
	private IComputeNodeService nodeService;
	
	@Override
	public ApiDescribeNodesReplyMsg handle(ApiDescribeNodesMsg msg) throws GCloudException {
		DescribeNodesParams params = BeanUtil.copyProperties(msg, DescribeNodesParams.class);
		PageResult<NodeBaseInfo> nodes = nodeService.describeNodes(params, msg.getCurrentUser());
		
		ApiDescribeNodesReplyMsg reply = new ApiDescribeNodesReplyMsg();
		reply.init(nodes);
		return reply;
	}

}
