package com.gcloud.controller.network.service;

import com.gcloud.controller.network.model.CreateNetworkParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.network.model.ExternalNetworkSetType;
import com.gcloud.header.network.model.VpcsItemType;
import com.gcloud.header.network.msg.api.CreateExternalNetworkMsg;
import com.gcloud.header.network.msg.api.DescribeExternalNetworksMsg;
import com.gcloud.header.network.msg.api.DescribeVpcsMsg;
import com.gcloud.header.network.msg.api.ModifyVpcAttributeMsg;

public interface INetworkService {
	PageResult<VpcsItemType> describeVpcs(DescribeVpcsMsg msg);
	String createNetwork(CreateNetworkParams params, CurrentUser currentUser);
	String createExternalNetwork(CreateExternalNetworkMsg msg);
	PageResult<ExternalNetworkSetType> describeNetworks(DescribeExternalNetworksMsg msg);
	void removeNetwork(String networkId);
	void updateNetwork(ModifyVpcAttributeMsg msg);
	void getNetworks(String id);
}
