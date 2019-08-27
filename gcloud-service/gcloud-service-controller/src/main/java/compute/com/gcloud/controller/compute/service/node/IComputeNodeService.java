package com.gcloud.controller.compute.service.node;

import com.gcloud.controller.compute.model.node.AttachNodeParams;
import com.gcloud.controller.compute.model.node.DescribeNodesParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.node.node.model.ComputeNodeInfo;
import com.gcloud.header.compute.msg.node.node.model.NodeBaseInfo;

public interface IComputeNodeService {

    void computeNodeConnect(ComputeNodeInfo computeNodeInfo, int nodeTimeout);

    PageResult<NodeBaseInfo> describeNodes(DescribeNodesParams params, CurrentUser currentUser);
    void attachNode(AttachNodeParams params, CurrentUser currentUser);
}
